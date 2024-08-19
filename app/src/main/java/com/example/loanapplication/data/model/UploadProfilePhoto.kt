package com.example.loanapplication.data.model


import androidx.annotation.Keep

@Keep

data class UploadProfilePhoto(
    var mobile: String,
    var photo: String
)