package com.dicoding.storyapp1.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp1.data.Result
import com.dicoding.storyapp1.data.UserRepository
import java.io.File

class UploadViewModel (private val repository: UserRepository) : ViewModel() {
    fun uploadImage(token: String, file: File, desc: String): LiveData<Result<String>> = repository.uploadImage(token, file, desc)
}