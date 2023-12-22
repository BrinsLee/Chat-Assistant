package com.brins.gpt.extensions

import io.getstream.result.Error
import io.getstream.result.Result
import io.getstream.result.call.Call


private val onSuccessStub: (Any) -> Unit = {}
private val onErrorStub: (Error) -> Unit = {}
public fun <T : Any> Call<T>.enqueue(
    onSuccess: (T) -> Unit = onSuccessStub,
    onError: (Error) -> Unit = onErrorStub
) {
    enqueue { result ->
        when (result) {
            is Result.Success -> onSuccess(result.value)
            is Result.Failure -> onError(result.value)
        }
    }
}