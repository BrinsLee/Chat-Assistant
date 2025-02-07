package com.brins.lib_network.utils

import dagger.hilt.InstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
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

    // 流式请求封装
    suspend fun <T : Any> safeStreamApiCall(
        apiCall: suspend () -> Response<ResponseBody>,
        onChunkReceived: (chunk: T) -> Unit,
        onComplete: () -> Unit,
        onError: (error: IOException) -> Unit,
        chunkParser: (chunk: String) -> T?
    ) {
        withContext(dispatcher) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.use { responseBody ->
                        val source = responseBody.source()
                        val buffer = source.buffer()
                        while (true) {
                            val line = buffer.readUtf8Line() ?: break
                            if (line.isBlank()) continue
                            if (line.startsWith("data: ")) {
                                val json = line.removePrefix("data: ").trim()
                                if (json == "[DONE]") {
                                    onComplete()
                                    break
                                }

                                // 解析并回调
                                chunkParser(json)?.let { parsedChunk ->
                                    onChunkReceived(parsedChunk)
                                }
                            }
                        }
                    }
                } else {
                    onError(IOException("Error occurred during getting safe Api result: ${response.errorBody()?.string()}"))
                }
            }catch (e: Exception) {

                when (e) {
                    is HttpException -> onError(IOException("HttpException: ${e.message}", e))
                    is IOException -> onError(IOException("Network error occurred: ${e.message}", e))
                    else -> onError(IOException("Unknown error occurred: ${e.message}", e))
                }
            }
        }
    }

    sealed class Result<out R> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val exception: IOException) : Result<Nothing>()
    }

}