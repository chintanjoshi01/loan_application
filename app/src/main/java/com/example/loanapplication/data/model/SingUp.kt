package com.example.loanapplication.data.model


import androidx.annotation.Keep

@Keep
data class SingUp(
    var mobile: String,
    var password: String,
    var username: String
)