package com.example.newsapp.screens.headlines

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.model.Article
import com.example.newsapp.util.Constants
import com.example.newsapp.util.Resource
import com.example.newsapp.viewModel.NewsViewModel


class HeadlinesFragment : Fragment(), NewsAdapter.OnItemClick {
    lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var retryButton: Button
    private lateinit var error: TextView
    private lateinit var itemExecuteError: CardView
    private lateinit var binding: FragmentHeadlinesBinding

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)
        itemExecuteError = view.findViewById(R.id.layoutError)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val lView: View = inflater.inflate(R.layout.item_error, null)

        retryButton = lView.findViewById(R.id.btRetry)
        error = lView.findViewById(R.id.tvErrorItem)
        viewModel = (activity as MainActivity).viewModel
        setUpHeadlineRecycler()
        retryButton.setOnClickListener {
            viewModel.getHeadlineNews("us")
        }
        viewModel.headline.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.DEFAULT_QUERY_PAGE + 2
                        isLastPage = viewModel.headlinesPage == totalPages
                        if(isLastPage) {
                            binding.rvHeadline.setPadding(0,0,0,0)
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

        })


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
                viewModel.getHeadlineNews("us")
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
    private fun setUpHeadlineRecycler() {
        newsAdapter = NewsAdapter(this)
        binding.rvHeadline.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HeadlinesFragment.scrollList)
            setHasFixedSize(true)
        }
    }

    override fun onItemViewClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }
        findNavController().navigate(R.id.action_headlinesFragment_to_articleFragment, bundle)
    }
}