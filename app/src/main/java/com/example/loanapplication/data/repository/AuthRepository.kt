package com.example.loanapplication.data.repository

import com.example.loanapplication.data.model.Message
import com.example.loanapplication.data.model.SingIn
import com.example.loanapplication.data.model.SingUp
import com.example.loanapplication.data.network.ApiService
import com.example.loanapplication.util.MyNetworkResult
import com.example.loanapplication.util.PREFERENCES_KEY_MOBILE
import com.example.loanapplication.util.PREFERENCES_KEY_USERNAME
import com.example.loanapplication.util.SharedPreferencesUtils
import com.google.gson.Gson
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferencesUtils: SharedPreferencesUtils
) {
    suspend fun singUp(
        emailId: String,
        password: String,
        mobile: String
    ): MyNetworkResult<Message> {
        val singUp = SingUp(mobile, password, emailId)
        val response = apiService.singUp(singUp)
        return if (response.isSuccessful) {
            MyNetworkResult.Success(response.body()!!)
        } else if (response.code() == 400) {
            val gson = Gson()
            val message = gson.fromJson(response.errorBody()?.string(), Message::class.java)
            MyNetworkResult.Error(Exception(message?.message))
        } else {
            MyNetworkResult.Error(Exception("Error"))
        }
    }

    suspend fun singIn(
        username: String,
        password: String,
        mobile: String,
    ): MyNetworkResult<Message> {
        val singUp = SingIn(password, username)
        val response = apiService.singIn(singUp)
        return if (response.isSuccessful) {
            sharedPreferencesUtils.setStingShard(PREFERENCES_KEY_USERNAME, username)
            sharedPreferencesUtils.setStingShard(PREFERENCES_KEY_MOBILE, mobile)
            MyNetworkResult.Success(response.body()!!)
        } else if (response.code() == 400) {
            val gson = Gson()
            val message = gson.fromJson(response.errorBody()?.string(), Message::class.java)
            MyNetworkResult.Error(Exception(message?.message))
        } else {
            MyNetworkResult.Error(Exception("Error"))
        }
    }


}
