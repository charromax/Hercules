package com.example.hercules.domain.models

import org.joda.time.DateTime

data class Message(
    val createdAt: String = DateTime.now().toDateTimeISO().toString(),
    val topic: String,
    val message: String
)

class ConnectionLostException(error: String) : Exception(error)
class InvalidMessageException(error: String) : Exception(error)