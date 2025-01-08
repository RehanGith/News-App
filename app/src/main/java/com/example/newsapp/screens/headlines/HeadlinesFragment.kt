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
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.util.Constants
import com.example.newsapp.viewModel.NewsViewModel


class HeadlinesFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var recycleView: RecyclerView
    lateinit var retryButton: Button
    lateinit var error: TextView
    lateinit var itemExecuteError: CardView
    lateinit var binding: FragmentHeadlinesBinding

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)
        itemExecuteError = view.findViewById(R.id.layoutError)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val lView: View = inflater.inflate(R.layout.item_error, null)

        retryButton = lView.findViewById(R.id.btRetry)
        error = lView.findViewById<TextView>(R.id.tvErrorItem)


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_headlinesFragment_to_articleFragment, bundle)
        }

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

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLatePage
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
    fun setUpHeadlineRecycler() {
        newsAdapter = NewsAdapter()
        binding.rvHeadline.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HeadlinesFragment.scrollList)
            setHasFixedSize(true)
        }
    }


}