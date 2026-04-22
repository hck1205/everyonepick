package com.everyonepick.core.data.network

sealed interface NetworkResult<out T> {
    data class Success<T>(val value: T) : NetworkResult<T>

    data class Failure(val throwable: Throwable) : NetworkResult<Nothing>
}

