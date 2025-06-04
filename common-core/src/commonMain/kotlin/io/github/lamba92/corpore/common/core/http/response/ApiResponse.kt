@file:Suppress("FunctionName")

package io.github.lamba92.corpore.common.core.http.response

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val isSuccess: Boolean = true,
    val error: Error? = null,
) {
    @Serializable
    data class Error(
        val message: String,
        val code: Int? = null,
        val stackTrace: List<String> = emptyList(),
    )
}

fun ApiResponse(
    errorMessage: String,
    errorCode: Int? = null,
    stackTrace: List<String> = emptyList(),
): ApiResponse<Nothing> =
    ApiResponse(
        isSuccess = false,
        error =
            ApiResponse.Error(
                message = errorMessage,
                code = errorCode,
                stackTrace = stackTrace,
            ),
    )
