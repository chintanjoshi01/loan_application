package com.example.loanapplication.data.model

data class SmsMessage(
    val sender: String,
    val body: String,
    val timestamp: Long,
    val type: String
)