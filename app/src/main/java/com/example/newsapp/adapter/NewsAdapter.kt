package com.example.newsapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.model.Article

class NewsAdapter(private val listener: OnItemClick):
    RecyclerView.Adapter<NewsAdapter.ArticleHolder>() {

    class ArticleHolder(binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        val articleImage = binding.articleImage
        val articleSource = binding.articleSource
        val articleDescription = binding.articleDescription
        val articleDateTime = binding.articleDateTime
        val articleTitle = binding.articleTitle
    }

    interface OnItemClick {
        fun onItemViewClick(article: Article)
    }

    private val differCallBack =object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this@NewsAdapter, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        return ArticleHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.articleImage)
        }
        holder.articleTitle.text = article.title
        holder.articleSource.text = article.source.name
        holder.articleDateTime.text = article.publishedAt
        holder.articleDescription.text = article.description
        holder.itemView.setOnClickListener {
            Log.d("NewsAdapter", "onBindViewHolder: $article")
            listener.onItemViewClick(article)
        }
    }

}