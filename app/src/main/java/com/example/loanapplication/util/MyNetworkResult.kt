package com.example.loanapplication.util


sealed class MyNetworkResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : MyNetworkResult<T>()
    data class Error(val exception: Exception) : MyNetworkResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}