package com.example.newsapp.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.model.Article
import retrofit2.Response

class NewsAdapter(private val context: Context, private val articleList: Response<List<Article>>):
    RecyclerView.Adapter<NewsAdapter.ArticleHolder>() {
    class ArticleHolder(binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        TODO("Not yet implemented")
    }
}