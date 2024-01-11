package com.dicoding.storyapp1.view.map

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp1.data.UserRepository

class MapsViewModel (private val repository: UserRepository) : ViewModel() {

    suspend fun getStoryWithLoc(token: String) = repository.getStoryWithLoc(token)
}