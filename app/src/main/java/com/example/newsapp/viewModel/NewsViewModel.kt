package com.example.newsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.newsapp.repository.NewsRepository

class NewsViewModel(app: Application, val newsRepository: NewsRepository): AndroidViewModel(app) {
}