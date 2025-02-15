package com.example.newsapp.screens.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.model.Article
import com.example.newsapp.viewModel.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: FragmentArticleBinding
    var article: Article? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)
        article = arguments?.getSerializable("article") as Article?
        newsViewModel = (activity as MainActivity).viewModel
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article?.url!!)
        }
        binding.fabButton.setOnClickListener{
            addToFavoriate(article)
        }
    }

    private fun addToFavoriate(article: Article?) {
        if (article != null) {
            newsViewModel.addToFavorite(article)
        }
    }
}