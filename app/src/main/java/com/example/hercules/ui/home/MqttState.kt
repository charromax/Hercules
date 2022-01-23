package com.example.hercules.ui.home

import com.example.hercules.domain.models.Message
import java.lang.Exception

data class MqttState(
    val message: Message?,
    val error: Exception?
)
