/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.mqtt

import com.example.hercules.domain.model.Message
import com.example.hercules.presentation.ui.home.TotemSubscriptionState

data class MqttState(
    val isMqttConnected: Boolean = false,
    val lastMessageReceived: Message? = null,
    val lastMessageSent: Message? = null,
    val snack: String? = null,
    val error: String? = null,
    val totemSubscriptionState: TotemSubscriptionState? = null
)
