package com.example.newsapp.screens.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.model.Article
import com.example.newsapp.util.Constants
import com.example.newsapp.util.Constants.Companion.TIME_DELAY
import com.example.newsapp.util.Resource
import com.example.newsapp.viewModel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment(R.layout.fragment_search), NewsAdapter.OnItemClick {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var binding: FragmentSearchBinding
    private lateinit var retryButton: Button
    private lateinit var error:TextView
    private lateinit var itemExecuteError: CardView

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        itemExecuteError = view.findViewById(R.id.layoutError)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val lView = inflater.inflate(R.layout.item_error, null)
        retryButton = lView.findViewById(R.id.btRetry)
        error = lView.findViewById(R.id.tvErrorItem)
        recycler = binding.rvSearch
        setUpRecyclerView()
        binding.edSearch.text?.let {
            if(it.isNotEmpty()) {
                newsViewModel.getSearchedNews(it.toString())
            }
        }
        newsViewModel = (activity as MainActivity).viewModel
        newsViewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.DEFAULT_QUERY_PAGE + 2
                        isLastPage = newsViewModel.searchNewsPage == totalPages
                        if(isLastPage) {
                            recycler.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Loading<*> -> {
                    showProgressBar()
                }
                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occured: $it", Toast.LENGTH_LONG).show()
                        showErrorMessage(it)
                    }
                }
            }

        }

        retryButton.setOnClickListener {
            if(binding.edSearch.text.toString().isNotEmpty()) {
                newsViewModel.getSearchedNews(binding.edSearch.text.toString())
            } else {
                hideErrorMessage()
            }
        }
        var job : Job? = null
        binding.edSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        newsViewModel.getSearchedNews(editable.toString())
                    }
                }
            }
        }

    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter(this)
        recycler.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFragment.scrollList)
            setHasFixedSize(true)
        }
    }

    var isError = false
    var isLoading = false
    var isScrolling = false
    var isLastPage = false

    private fun hideProgressBar() {
        binding.loadingProgressbar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        binding.loadingProgressbar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun showErrorMessage(message: String) {
        itemExecuteError.visibility = View.VISIBLE
        error.text = message
        isError = true
    }
    private fun hideErrorMessage() {
        itemExecuteError.visibility = View.INVISIBLE
        isError = false
    }

    private val scrollList = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItem + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItem >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.DEFAULT_QUERY_PAGE
            val shouldPaginate = isNoErrors && isNotAtBeginning && isAtLastItem && isNotLoadingAndNotLastPage && isTotalMoreThanVisible
            if(shouldPaginate) {
                newsViewModel.getSearchedNews(binding.edSearch.text.toString())
                isScrolling= false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun onItemViewClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }
        findNavController().navigate(R.id.action_searchFragment_to_articleFragment, bundle)
    }

}

