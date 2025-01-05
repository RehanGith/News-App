package com.example.newsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.model.Article
import retrofit2.Response

class NewsAdapter(private val context: Context):
    RecyclerView.Adapter<NewsAdapter.ArticleHolder>() {
    class ArticleHolder(binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        val articleImage = binding.articleImage
        val articleSource = binding.articleSource
        val articleDescription = binding.articleDescription
        val articleDateTime = binding.articleDateTime
        val articleTitle = binding.articleTitle
    }
    private val differCallBack =object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this@NewsAdapter, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        return ArticleHolder(ItemNewsBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickList: ((Article) -> Unit)? = null
    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        Glide.with(context).load(differ.currentList[position].urlToImage).into(holder.articleImage)
        holder.articleTitle.text = differ.currentList[position].title
        holder.articleSource.text = differ.currentList[position].source.name
        holder.articleDateTime.text = differ.currentList[position].publishedAt
        holder.articleDescription.text = differ.currentList[position].description

        holder.itemView.setOnClickListener {
            onItemClickList?.let {
                it(differ.currentList[position])
            }
        }
    }
}