package com.example.loanapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loanapplication.R
import com.example.loanapplication.data.model.SingUpFormState
import com.example.loanapplication.data.repository.AuthRepository
import com.example.loanapplication.util.MyNetworkResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _singUpForm = MutableLiveData<SingUpFormState>()
    val loginFormState: LiveData<SingUpFormState> = _singUpForm

    private val _message = MutableLiveData<String>()
    val message = _message
    private val _isSingin = MutableLiveData<Boolean>()
    val isSingin get() = _isSingin

    fun singIn(username: String, password: String, mobile: String) {
        viewModelScope.launch {
            val result = repository.singIn(username, password, mobile)
            if (result is MyNetworkResult.Success) {
                _isSingin.value = true
                _message.value = result.data.message
            } else if (result is MyNetworkResult.Error) {
                _isSingin.value = false
                _message.value = result.exception.message
            }
        }
    }

    fun singUp(username: String, password: String, mobile: String) {
        viewModelScope.launch {
            val result = repository.singUp(username, password, mobile)
            if (result is MyNetworkResult.Success) {
                _message.value = result.data.message
            } else if (result is MyNetworkResult.Error) {
                _message.value = result.exception.message
            }
        }
    }

    fun loginDataChanged(
        username: String?,
        password: String?,
        confirmPassword: String?,
        mobile: String?
    ) {
        val emailError =
            if (username != null && !isEmailValid(username)) R.string.invalid_username else null
        val passwordError =
            if (password != null && !isPasswordValid(password)) R.string.invalid_password else null
        val mobileError =
            if (mobile != null && !isMobileValid(mobile)) R.string.invalid_mobile else null
        val confirmPasswordError = if (confirmPassword != null && !isConfirmPasswordValid(
                password ?: "",
                confirmPassword
            )
        ) R.string.invalid_confirm_password else null

        _singUpForm.value = SingUpFormState(
            emailIdError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
            mobileError = mobileError,
            isDataValid = emailError == null && passwordError == null && mobileError == null && confirmPasswordError == null
        )
    }


    private fun isConfirmPasswordValid(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    private fun isEmailValid(emailId: String): Boolean {
        return emailId.isNotBlank()

    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isMobileValid(mobile: String): Boolean {
        return mobile.length == 10
    }
}