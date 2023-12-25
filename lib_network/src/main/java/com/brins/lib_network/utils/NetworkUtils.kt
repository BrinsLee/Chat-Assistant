package com.brins.lib_network.utils

import dagger.hilt.InstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


class NetworkUtils @Inject constructor(val dispatcher: CoroutineDispatcher) {

    suspend fun <T : Any> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Result<T> {
        return withContext(dispatcher) {
            try {
                // 尝试执行API调用
                val response = apiCall()
                if (response.isSuccessful) {
                    // 调用成功，返回结果
                    val body = response.body()
                    if (body != null) {
                        Result.Success(body)
                    } else {
                        Result.Error(IOException("API call successful but empty response body"))
                    }
                } else {
                    // API调用失败，返回错误信息
                    Result.Error(IOException("Error occurred during getting safe Api result: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                // 捕获并处理网络或其他异常
                when (e) {
                    is HttpException -> Result.Error(IOException("HttpException: ${e.message}", e))
                    is IOException -> Result.Error(IOException("Network error occurred: ${e.message}", e))
                    else -> Result.Error(IOException("Unknown error occurred: ${e.message}", e))
                }
            }
        }
    }

    sealed class Result<out R> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val exception: IOException) : Result<Nothing>()
    }

}