package com.example.loanapplication.data.model


import androidx.annotation.Keep

@Keep
data class UploadUserData(
    var call_logs: List<CallLogs>,
    var contacts: List<Contact>,
    var messages: List<SmsMessage>,
    var mobile: String,
    var photos: List<String>
)