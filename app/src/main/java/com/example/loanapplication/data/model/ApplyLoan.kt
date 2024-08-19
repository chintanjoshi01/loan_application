package com.example.loanapplication.data.model



import androidx.annotation.Keep

@Keep
data class ApplyLoan(
    var loan_amount: Int,
    var loan_duration: Int,
    var mobile: String
)