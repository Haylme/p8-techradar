package com.example.techradar.model

data class SimpleResponse<T>(
    val status: Status,
    val data: T? = null,
    val exception: Exception? = null
) {
    sealed class Status {
        object Success : Status()
        object Failure : Status()
        object Initial : Status() // Added to represent the initial state
    }

    companion object {
        fun <T> success(data: T): SimpleResponse<T> {
            return SimpleResponse(status = Status.Success, data = data)
        }

        fun <T> failure(exception: Exception): SimpleResponse<T> {
            return SimpleResponse(status = Status.Failure, exception = exception)
        }

        fun <T> initial(): SimpleResponse<T> {
            return SimpleResponse(status = Status.Initial)
        }
    }


}