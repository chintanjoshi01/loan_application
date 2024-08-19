package com.example.loanapplication.data.network

import com.example.loanapplication.data.model.ApplyLoan
import com.example.loanapplication.data.model.LoanHistory
import com.example.loanapplication.data.model.Message
import com.example.loanapplication.data.model.SingIn
import com.example.loanapplication.data.model.SingUp
import com.example.loanapplication.data.model.UploadProfilePhoto
import com.example.loanapplication.data.model.UploadUserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @POST("sign_up")
    suspend fun singUp(@Body singUp: SingUp): Response<Message>

    @POST("sign_in")
    suspend fun singIn(@Body singUp: SingIn): Response<Message>

    @POST("apply_loan")
    suspend fun applyLoan(@Body applyLoan: ApplyLoan): Response<Message>

    @POST("upload_photo")
    suspend fun updateProfilePhoto(@Body uploadProfilePhoto: UploadProfilePhoto): Response<Message>

    @POST("upload_user_data")
    suspend fun uploadUserData(@Body uploadUserData: UploadUserData): Response<Message>

    @GET("loan_history")
    suspend fun getLoanHistory(@Query("mobile") mobileNumber: String ): Response<LoanHistory>
}