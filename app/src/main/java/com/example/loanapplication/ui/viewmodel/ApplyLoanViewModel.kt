package com.example.loanapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loanapplication.data.model.ApplyLoan
import com.example.loanapplication.data.repository.ApplyLoanRepository
import com.example.loanapplication.util.MyNetworkResult
import com.example.loanapplication.util.PREFERENCES_DEFULT_MOBILE
import com.example.loanapplication.util.PREFERENCES_KEY_MOBILE
import com.example.loanapplication.util.SharedPreferencesUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class ApplyLoanViewModel @Inject constructor(
    private val repository: ApplyLoanRepository,
    private val sharedPreferencesUtils: SharedPreferencesUtils
) :
    ViewModel() {

    private val _message = MutableLiveData<String>()
    val message = _message


    fun applyLoan(amount: String, duration: String) {
        Log.d("ApplyLoanViewModel", "applyLoan: $amount $duration")
        viewModelScope.launch {
            val mobile = sharedPreferencesUtils.getStringShard(
                PREFERENCES_KEY_MOBILE, PREFERENCES_DEFULT_MOBILE
            )
            val applyLoan = mobile?.let {
                ApplyLoan(
                    amount.toInt(), duration.toInt(), it
                )
            }
            Log.d("ApplyLoanViewModel", "applyLoan: $applyLoan")
            val result = applyLoan?.let { repository.applyLoan(it) }
            if (result is MyNetworkResult.Success) {
                Log.d("ApplyLoanViewModel", "Message : ${result.data.message}")
                _message.value = result.data.message
            } else if (result is MyNetworkResult.Error) {
                Log.d("ApplyLoanViewModel", "Message : ${result.exception.message}")
                _message.value = result.exception.message
            }
        }
    }


}