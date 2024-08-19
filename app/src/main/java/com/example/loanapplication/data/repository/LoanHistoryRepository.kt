package com.example.loanapplication.data.repository

import com.example.loanapplication.data.model.LoanHistory
import com.example.loanapplication.data.network.ApiService
import com.example.loanapplication.util.MyNetworkResult
import com.example.loanapplication.util.PREFERENCES_DEFULT_MOBILE
import com.example.loanapplication.util.PREFERENCES_KEY_MOBILE
import com.example.loanapplication.util.SharedPreferencesUtils
import javax.inject.Inject

class LoanHistoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferencesUtils
) {


    suspend fun loanHistory(): MyNetworkResult<LoanHistory> {
        val mobileNumber = sharedPreferences.getStringShard(
            PREFERENCES_KEY_MOBILE,
            PREFERENCES_DEFULT_MOBILE
        ) ?: " "

        val response = apiService.getLoanHistory(mobileNumber)
        return if (response.isSuccessful) {
            MyNetworkResult.Success(response.body()!!)
        } else {
            MyNetworkResult.Error(Exception("Error"))
        }
    }
}