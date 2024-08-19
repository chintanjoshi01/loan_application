package com.example.loanapplication.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loanapplication.data.model.LoanHistory
import com.example.loanapplication.data.repository.LoanHistoryRepository
import com.example.loanapplication.util.MyNetworkResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoanHistoryViewModel @Inject constructor(private val repository: LoanHistoryRepository) :
    ViewModel() {

    private val _loanList = MutableLiveData<LoanHistory>()
    val loanList get() = _loanList
    private val message = MutableLiveData<String>()
    val errorMessage get() = message

    init {
        try {
            getLoansHistory()
        } catch (e: Exception) {

        }

    }

    fun getLoansHistory() {
        viewModelScope.launch {
            val response = repository.loanHistory()
            if (response is MyNetworkResult.Success) {
                _loanList.value = response.data
            } else if (response is MyNetworkResult.Error) {
                message.value = response.exception.message
            }
        }
    }


}