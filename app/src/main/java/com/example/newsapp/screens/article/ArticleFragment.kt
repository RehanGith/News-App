package com.example.newsapp.screens.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.model.Article
import com.example.newsapp.viewModel.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: FragmentArticleBinding
    lateinit var args: Bundle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        // TODO receive the article through arguments and initialize viewModel

        binding.webView.apply {
            webViewClient = WebViewClient()

        }
        binding.fabButton.setOnClickListener{

        }
    }
}