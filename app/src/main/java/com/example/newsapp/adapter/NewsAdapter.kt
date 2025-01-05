package com.example.newsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.model.Article
import retrofit2.Response

class NewsAdapter(private val context: Context, private val articleList: ArrayList<Article>):
    RecyclerView.Adapter<NewsAdapter.ArticleHolder>() {
    class ArticleHolder(binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        val articleImage = binding.articleImage
        val articleSource = binding.articleSource
        val articleDescription = binding.articleDescription
        val articleDateTime = binding.articleDateTime
        val articleTitle = binding.articleTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        return ArticleHolder(ItemNewsBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {

    }
}