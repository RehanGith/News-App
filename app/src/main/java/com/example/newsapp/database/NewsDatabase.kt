package com.example.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.api.ApiInterface
import com.example.newsapp.model.Article


@Database( entities = [Article::class], version = 2)
@TypeConverters(Converter::class)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: NewsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NewsDatabase::class.java,
                "newDatabase_db.db"
            )
                .fallbackToDestructiveMigration().build()
    }
}