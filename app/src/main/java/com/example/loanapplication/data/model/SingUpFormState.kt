package com.example.loanapplication.data.model

data class SingUpFormState(
    val emailIdError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val mobileError: Int? = null,
    val isDataValid: Boolean = false
)