package com.example.newsapp.model

data class newsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)