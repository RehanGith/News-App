package com.example.newsapp.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.model.newsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app: Application, val newsRepository: NewsRepository): AndroidViewModel(app) {
    val headline: MutableLiveData<Resource<newsResponse>> = MutableLiveData()
    private var headlinesPage  = 1
    private var headlinesResponse: newsResponse? = null

    val searchNews: MutableLiveData<Resource<newsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchResponse : newsResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    private fun handleHeadlineResponse(resource: Response<newsResponse>): Resource<newsResponse> {
        if(resource.isSuccessful) {
            resource.body()?.let { resultResponse ->
                headlinesPage++
                if(headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    val oldArticle = headlinesResponse!!.articles
                    val newArticle = resultResponse.articles
                    oldArticle.addAll(newArticle)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(resource.message())
    }

    private fun handleSearchResponse(resource: Response<newsResponse>): Resource<newsResponse> {

        if(resource.isSuccessful) {
            resource.body()?.let { resultResponse ->
                if(searchResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticle = searchResponse!!.articles
                    val newArticle = resultResponse.articles
                    oldArticle.addAll(newArticle)
                }
                return Resource.Success(searchResponse ?: resultResponse)
            }
        }
        return Resource.Error(resource.message())
    }
    fun addToFavorite(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun getAllFavorite() = newsRepository.getAllArticle()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
    fun internetConnectivity(context: Context): Boolean? {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }?: false
        }
    }
    private suspend fun headlineInternet(countryCode: String) {
        headline.postValue(Resource.Loading())
        try{
            if (internetConnectivity(this.getApplication()) == true) {
                val response = newsRepository.getHeadlines(countryCode, headlinesPage)
                headline.postValue(handleHeadlineResponse(response))
            } else {
                headline.postValue(Resource.Error("no Internet"))
            }
        } catch (t : Throwable) {
            when(t) {
                is IOException -> headline.postValue(Resource.Error("Unable to connect to Internet"))
                else -> headline.postValue(Resource.Error("no Internet"))
            }
        }
    }
    private suspend fun searchInternet(searchQuery: String) {
        newSearchQuery = searchQuery
        headline.postValue(Resource.Loading())
        try{
            if (internetConnectivity(this.getApplication()) == true) {
                val response = newsRepository.getSearchedArticles(searchQuery, searchNewsPage)
                searchNews.postValue(handleHeadlineResponse(response))
            } else {
                searchNews.postValue(Resource.Error("no Internet"))
            }
        } catch (t : Throwable) {
            when(t) {
                is IOException -> searchNews.postValue(Resource.Error("Unable to connect to Internet"))
                else -> searchNews.postValue(Resource.Error("no Internet"))
            }
        }
    }

}


