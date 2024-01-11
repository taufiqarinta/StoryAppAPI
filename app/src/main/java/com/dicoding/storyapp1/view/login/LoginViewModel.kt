package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp1.data.UserRepository

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun login(email: String, pass: String) = repository.login(email, pass)
}