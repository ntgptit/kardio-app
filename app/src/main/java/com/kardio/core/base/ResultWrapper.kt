// File: app/src/main/java/com/kardio/core/base/ResultWrapper.kt
package com.kardio.core.base

/**
 * Wrapper class to handle results from repositories.
 * Helps in propagating success or error states throughout the app.
 */
sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: Throwable) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()

    /**
     * Helper function to handle the result in a more concise way.
     */
    inline fun onSuccess(action: (T) -> Unit): ResultWrapper<T> {
        if (this is Success) action(data)
        return this
    }

    /**
     * Helper function to handle errors in a more concise way.
     */
    inline fun onError(action: (Throwable) -> Unit): ResultWrapper<T> {
        if (this is Error) action(exception)
        return this
    }

    /**
     * Helper function to handle loading state in a more concise way.
     */
    inline fun onLoading(action: () -> Unit): ResultWrapper<T> {
        if (this is Loading) action()
        return this
    }
}