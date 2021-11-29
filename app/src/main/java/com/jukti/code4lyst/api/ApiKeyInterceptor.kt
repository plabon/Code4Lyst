package com.jukti.code4lyst.api

import com.jukti.unrd.utilities.API_KEY
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class ApiKeyInterceptor():Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        builder.addHeader("x-api-key", API_KEY)
        return chain.proceed(builder.build())
    }
}