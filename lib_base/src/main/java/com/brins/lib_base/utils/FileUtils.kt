package com.brins.lib_base.utils

import android.content.Context
import io.getstream.chat.android.ui.common.utils.StreamFileUtil
import io.getstream.result.Error
import io.getstream.result.Result
import io.getstream.result.flatMap
import java.io.File

object FileUtils {

    const val CACHE_DIR_NAME = "gpt_cache"

    fun isCacheFileExist(context: Context, fileName: String): Result<Boolean> = try {
        getOrCreateStreamCacheDir(context).flatMap { Result.Success(File(it, fileName).exists()) }
    } catch (e: Exception) {
        Result.Failure(
            Error.ThrowableError(
                message = "Could not get or create the file.",
                cause = e,
            ),
        )
    }
    fun createFileInCacheDir(context: Context, fileName: String): Result<File> = try {
        getOrCreateStreamCacheDir(context).flatMap { Result.Success(File(it, fileName)) }
    } catch (e: Exception) {
        Result.Failure(
            Error.ThrowableError(
                message = "Could not get or create the file.",
                cause = e,
            ),
        )
    }

    @Suppress("TooGenericExceptionCaught")
    private fun getOrCreateStreamCacheDir(
        context: Context,
    ): Result<File> {
        return try {
            val file = File(context.cacheDir, CACHE_DIR_NAME).also { cacheDir ->
                cacheDir.mkdirs()
            }

            Result.Success(file)
        } catch (e: Exception) {
            Result.Failure(
                Error.ThrowableError(
                    message = "Could not get or create the cache directory",
                    cause = e,
                ),
            )
        }
    }

    public fun clearStreamCache(
        context: Context,
    ): Result<Unit> {
        return try {
            val directory = File(context.cacheDir, CACHE_DIR_NAME)
            directory.deleteRecursively()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(
                Error.ThrowableError(
                    message = "Could clear the cache directory",
                    cause = e,
                ),
            )
        }
    }
}