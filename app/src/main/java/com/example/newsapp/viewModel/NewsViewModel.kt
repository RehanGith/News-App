package com.example.newsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.model.newsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import okhttp3.internal.toImmutableList
import retrofit2.Response

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
                    val oldArticle = headlinesResponse!!.articles.toMutableList()
                    val newArticle = resultResponse.articles
                    oldArticle.addAll(newArticle)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(resource.message())
    }
}

