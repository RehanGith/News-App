package com.example.newsapp.screens.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.model.Article
import com.example.newsapp.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: FragmentArticleBinding
    private var article :  Article? = null
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
            article?.let { it1 -> newsViewModel.addToFavorite(it1) }
            Snackbar.make(view,"Article saved successfully",Snackbar.LENGTH_SHORT).show()
            binding.fabButton.setImageResource(R.drawable.ic_filled_fav)

        }
    }
}