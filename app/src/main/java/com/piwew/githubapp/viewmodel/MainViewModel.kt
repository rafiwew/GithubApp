package com.piwew.githubapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.piwew.githubapp.data.response.GithubResponse
import com.piwew.githubapp.data.response.ItemsItem
import com.piwew.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _userItem = MutableLiveData<ArrayList<ItemsItem>>()
    val userItem: LiveData<ArrayList<ItemsItem>> = _userItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun findUser(username: String) {
        try {
            _isLoading.value = true
            val client = ApiConfig.getApiService().searchUser(username)
            client.enqueue(object : Callback<GithubResponse> {
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("LENGTH: ", "Length: ${responseBody?.items?.size}")
                        if (responseBody != null) {
                            _userItem.value = ArrayList(responseBody.items)
                        } else {
                            _isLoading.value = true
                            _userItem.value = ArrayList()
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()} ")
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        } catch (e: Exception) {
            Log.d("MainViewModel", e.toString())
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
        private var username = "rafi"
    }

    private val randomUsername = arrayOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z"
    )

    private fun generateRandomUsername() {
        val randomIndex = Random.nextInt(randomUsername.size)
        username = randomUsername[randomIndex]
    }

    init {
        generateRandomUsername()
        findUser(username)
    }

}