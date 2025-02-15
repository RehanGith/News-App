package com.example.newsapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.database.NewsDatabase
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.viewModel.NewsViewModel
import com.example.newsapp.viewModel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViewModel()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
    private fun initializeViewModel() {
        val newsRepository = NewsRepository(NewsDatabase(this))
        val viewModelFactory = ViewModelFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}