package com.example.hercules.ui.home

import com.example.hercules.domain.models.Message
import java.lang.Exception

data class MqttState(
    val lastMessageReceived: Message?,
    val lastMessageSent: Message?,
    val error: Exception?
)
