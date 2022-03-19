/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.utils

sealed class Result<T>
data class Success<T>(val value: T) : Result<T>()
data class Failure<T>(val error: Exception) : Result<T>()