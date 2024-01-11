package com.dicoding.storyapp1.view.detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyapp1.data.Stories
import com.dicoding.storyapp1.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(STORY, Stories::class.java)
        } else {
            intent.getParcelableExtra(STORY)
        }

        if (id != null) {
            setupData(id)
        }
    }

    private fun setupData(data: Stories) {
        binding.tvUsername.text = data.name
        binding.tvDesc.text = data.description
        Glide.with(this).load(data.photoUrl).into(binding.imgDetail)
    }

    companion object{
        val STORY = "story"
    }
}