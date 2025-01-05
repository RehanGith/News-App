package com.example.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsapp.model.Article

@Dao
interface ArticleDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun addArticle(article: Article): Long

    @Query("SELECT * FROM article")
    fun getAllArticle(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}