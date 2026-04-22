package com.everyonepick.core.data.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRequestExecutor @Inject constructor() {
    suspend fun <T> execute(block: suspend () -> T): NetworkResult<T> =
        try {
            NetworkResult.Success(block())
        } catch (throwable: Throwable) {
            NetworkResult.Failure(throwable)
        }
}

