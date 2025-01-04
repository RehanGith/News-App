package com.example.newsapp.api

import com.example.newsapp.model.newsResponse
import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNo: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ) : Response<newsResponse>

    @GET("v2/everything")
    suspend fun getSearchedNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNo: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ) : Response<newsResponse>
}