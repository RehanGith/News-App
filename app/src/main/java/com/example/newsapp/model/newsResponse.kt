package com.example.newsapp.model

data class newsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)