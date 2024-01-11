package com.dicoding.storyapp1.di

import com.dicoding.storyapp1.data.ApiConfig
import com.dicoding.storyapp1.data.UserRepository

object Injection {
    fun provideRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}