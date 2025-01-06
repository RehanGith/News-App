package com.example.newsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.model.newsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Responce

class NewsViewModel(app: Application, val newsRepository: NewsRepository): AndroidViewModel(app) {
    val headline: MutableLiveData<Responce<newsResponse>> = MutableLiveData()
    val headlinesPage  = 1
    val headlinesResponse: newsResponse? = null

    val searchNews: MutableLiveData<Responce<newsResponse>> = MutableLiveData()
    val searchNewsPage = 1
    val searchResponse : newsResponse? = null
}