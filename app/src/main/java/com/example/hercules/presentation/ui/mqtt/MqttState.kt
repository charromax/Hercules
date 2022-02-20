/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.mqtt

import com.example.hercules.domain.model.Message

data class MqttState(
    val isMqttConnected: Boolean = false,
    val isMqttSubscribed: Boolean = false,
    val lastMessageReceived: Message? = null,
    val lastMessageSent: Message? = null,
    val snack: String? = null,
    val error: String? = null,
    val subscriptionMap: Map<String, Boolean>? = null
)
