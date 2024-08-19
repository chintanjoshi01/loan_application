package com.example.loanapplication.data.repository

import com.example.loanapplication.data.model.ApplyLoan
import com.example.loanapplication.data.model.Message
import com.example.loanapplication.data.network.ApiService
import com.example.loanapplication.util.MyNetworkResult
import com.google.gson.Gson
import javax.inject.Inject

class ApplyLoanRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun applyLoan(applyLoan: ApplyLoan): MyNetworkResult<Message> {
        val response = apiService.applyLoan(applyLoan)
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

}