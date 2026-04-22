package com.everyonepick.core.data.network

import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Retrofit

@Singleton
class ApiFactory @Inject constructor(
    private val retrofit: Retrofit,
) {
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}

