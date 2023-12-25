package com.brins.lib_network.error

private const val NETWORK_FAILED_ERROR_CODE = 1000
enum class ErrorCode(public val code: Int, public val description: String) {
    NETWORK_FAILED(NETWORK_FAILED_ERROR_CODE, "Response is failed. See cause")
}