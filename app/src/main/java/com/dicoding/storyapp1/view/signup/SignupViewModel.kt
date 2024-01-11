package com.dicoding.storyapp1.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp1.data.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    fun signup(name: String, email: String, pass: String) = repository.signup(name, email, pass)

}