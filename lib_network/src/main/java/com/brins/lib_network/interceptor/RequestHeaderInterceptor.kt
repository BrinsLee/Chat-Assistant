package com.brins.lib_network.interceptor

import com.brins.lib_base.BuildConfig
import com.brins.lib_base.config.Authorization
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RequestHeaderInterceptor @Inject constructor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val url = originalUrl.newBuilder().build()
        val requestBuilder = originalRequest.newBuilder().url(url).apply {
            val authorization = "Bearer ${BuildConfig.API_KEY}"
            addHeader(Authorization, authorization)
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }


}
