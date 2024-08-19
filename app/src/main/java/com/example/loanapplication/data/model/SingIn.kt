package com.example.loanapplication.data.model


import androidx.annotation.Keep

@Keep
data class SingIn(
    var password: String,
    var username: String
)