package com.example.chatappnative.data.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class ResponseCodeCheckInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        when (response.code) {
            401 -> {
                throw APIException(401, "Unauthorized")
            }

            403 -> {
                throw APIException(403, "Forbidden")
            }

            405 -> {
                throw APIException(405, "Method Not Allowed")
            }

            429 -> {
                throw APIException(429, "Too Many Requests")
            }

            500 -> {
                Log.d("ResponseCodeCheckInterceptor", response.toString())
                return response
            }

            400, 402, 404 -> {
                throw APIException(response.code, response.message)
            }

            else -> {
                return response
            }
        }
    }

    companion object {
        private const val TAG = "RespCacheInterceptor"
    }
}