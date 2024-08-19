package com.example.loanapplication.data.model


import androidx.annotation.Keep

@Keep
data class Loan(
    var amount: Double,
    var duration: Int,
    var status: String
)