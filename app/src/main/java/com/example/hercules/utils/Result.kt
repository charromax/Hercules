package com.example.hercules.utils

sealed class Result<T>
data class Success<T>(val value: T) : Result<T>()
data class Failure<T>(val error: Throwable) : Result<T>()