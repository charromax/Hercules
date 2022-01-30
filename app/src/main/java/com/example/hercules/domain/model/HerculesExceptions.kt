/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.model

sealed class HerculesExceptions(val error: String) : Exception(error) {
    class ConnectionLostException(error: String) : HerculesExceptions(error)
    class UnableToConnectException(error: String) : HerculesExceptions(error)
    class InvalidMessageException(error: String) : HerculesExceptions(error)
    class EmptyTopicException(error: String) : HerculesExceptions(error)
}

