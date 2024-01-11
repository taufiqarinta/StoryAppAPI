package com.dicoding.storyapp1.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dicoding.storyapp1.data.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getStories(token:String) = repository.getStories(token).cachedIn(viewModelScope)

}