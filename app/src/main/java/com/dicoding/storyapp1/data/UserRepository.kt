package com.dicoding.storyapp1.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
) {
    private val signupCheck = MediatorLiveData<Result<UserModel>>()
    private val logInResult = MediatorLiveData<Result<String>>()
    private val storiesResult = MediatorLiveData<Result<List<Stories>>>()
    private val upStoryResult = MediatorLiveData<Result<String>>()
    private val storyLocResult = MediatorLiveData<Result<List<Stories>>>()

    fun signup(name: String, email: String, pass: String): LiveData<Result<UserModel>> {

        val client = apiService.register(name, email, pass)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {
                try {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val user = UserModel(email, "null")
                            signupCheck.value = Result.Success(user)
                        }
                    } else {
                        throw HttpException(response)
                    }
                }catch (e: HttpException) {
                    val errorString = e.response()?.errorBody()?.string()
                    val error = Gson().fromJson(errorString, RegisterResponse::class.java)
                    val erMessage = error.message
                    logInResult.value = Result.Error(erMessage!!)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                signupCheck.value = Result.Error(t.message.toString())
            }

        })
        return signupCheck
    }

    fun login(email: String, pass: String): LiveData<Result<String>> {

        val client = apiService.login(email, pass)
        client.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {
                try {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            logInResult.value = Result.Success(responseBody.loginResult!!.token!!)
                        }
                    } else {
                        throw HttpException(response)
                    }
                }catch (e: HttpException) {
                    val errorString = e.response()?.errorBody()?.string()
                    val error = Gson().fromJson(errorString, RegisterResponse::class.java)
                    val erMessage = error.message
                    logInResult.value = Result.Error(erMessage!!)
                }

            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                logInResult.value = Result.Error(t.message.toString())
            }

        })
        return logInResult
    }

    fun getStories(token: String): LiveData<PagingData<Stories>> {
        val newToken = "Bearer $token"
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, newToken)
            }
        ).liveData
    }

    fun uploadImage(token: String, imgFile: File, desc: String): LiveData<Result<String>> {
        val newToken = "Bearer $token"
        val requestBody = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imgFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imgFile.name,
            requestImageFile
        )
        val successResponse = apiService.uploadStory(newToken, multipartBody, requestBody)
        successResponse.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            upStoryResult.value = Result.Success(response.body()?.message!!)
                        }
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    upStoryResult.value = Result.Error(errorResponse.message!!)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                upStoryResult.value = Result.Error(t.message.toString())
            }

        })
        return upStoryResult
    }

    suspend fun getStoryWithLoc(token: String): LiveData<Result<List<Stories>>> {
        val newToken = "Bearer $token"
        try {
            val client = apiService.getStoriesWithLocation(newToken)
            if (client.isSuccessful) {
                val stories = client.body()?.listStory
                storyLocResult.value = Result.Success(stories!!)
            } else {
                throw HttpException(client)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            val erMessage = errorResponse.message
            storyLocResult.value = Result.Error(erMessage!!)
        }

        return storyLocResult
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }

    }
}