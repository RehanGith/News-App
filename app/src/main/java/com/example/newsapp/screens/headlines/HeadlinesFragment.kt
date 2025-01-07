package com.example.newsapp.screens.headlines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
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

    private fun hideProgressBar() {
        binding.loadingProgressbar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        binding.loadingProgressbar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun showErrorMessage() {
        itemExecuteError.visibility = View.VISIBLE
        isError = true
    }
    private fun hideErrorMessage(message: String) {
        itemExecuteError.visibility = View.INVISIBLE
        error.text = message
        isError = false
    }
    val scrollList = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }
    }

}