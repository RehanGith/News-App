package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitsInstance
import com.example.newsapp.database.NewsDatabase
import com.example.newsapp.model.Article

class NewsRepository(val db: NewsDatabase) {

    suspend fun getHeadlines(countryCode: String, pageNo :Int) =
        RetrofitsInstance.api.getHeadlines(countryCode, pageNo)

    suspend fun getSearchedArticles(searchQuery: String, pageNo: Int) =
        RetrofitsInstance.api.getSearchedNews(searchQuery, pageNo)

    suspend fun upsert(article : Article) =
        db.getArticleDao().upsert(article)
    fun getAllArticle() =
        db.getArticleDao().getAllArticle()
    suspend fun deleteArticle(article: Article) =
        db.getArticleDao().deleteArticle(article)
}