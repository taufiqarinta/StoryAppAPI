package com.dicoding.storyapp1.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp1.data.Constants.TOKEN
import com.dicoding.storyapp1.data.Preference
import com.dicoding.storyapp1.databinding.ActivityMainBinding
import com.dicoding.storyapp1.view.LoadingStateAdapter
import com.dicoding.storyapp1.view.ViewModelFactory
import com.dicoding.storyapp1.view.detail.DetailActivity
import com.dicoding.storyapp1.view.map.MapsActivity
import com.dicoding.storyapp1.view.upload.UploadActivity
import com.dicoding.storyapp1.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance()
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRV()
        setupObserver()
        setupAction()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupRV(){
        val layaoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layaoutManager
    }

    private fun setupObserver() {
        val sharedPreference = Preference.init(this, "session")
        val token = sharedPreference.getString(TOKEN,"")

        if (token != ""){
            setupStories(token!!)
//            lifecycleScope.launch {
//                viewModel.getStories(token!!).observe(this@MainActivity) {
//                    if (it is Result.Success) {
//                        setupStories(it.data)
//                    } else if (it is Result.Error) {
//                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
        }
    }

    private fun setupStories(token: String) {
        val mainAdapter = RvStoriesAdapter()

        binding.rvStories.adapter = mainAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                mainAdapter.retry()
            }
        )
        viewModel.getStories(token).observe(this) {
            mainAdapter.submitData(lifecycle, it)
        }
        mainAdapter.onClick = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.STORY, it)
            startActivity(intent)
        }
    }

    private fun setupAction() {

        binding.fbtnAddStory.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            Preference.logout(this)
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.btnMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

}