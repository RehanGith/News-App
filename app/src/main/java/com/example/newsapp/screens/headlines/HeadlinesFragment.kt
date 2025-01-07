package com.example.newsapp.screens.headlines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.viewModel.NewsViewModel


class HeadlinesFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var recycleView: RecyclerView
    lateinit var retryButton: Button
    lateinit var error: TextView
    lateinit var itemExecuteError: CardView
    lateinit var binding: FragmentHeadlinesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)


    }
    var isError = false
    var isLoading = false
    var isScrolling = false
    var isLatePage = false



}