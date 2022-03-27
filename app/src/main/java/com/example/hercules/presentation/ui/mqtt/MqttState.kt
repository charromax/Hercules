/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.mqtt

import com.example.hercules.data.remote.response.MagSensorPayload
import com.example.hercules.data.remote.response.TotemResponse
import com.example.hercules.data.remote.response.WaterPumpPayload
import com.example.hercules.domain.model.Message

data class MqttState(
    val isMqttConnected: Boolean = false,
    val isMqttSubscribed: Boolean = false,
    val lastMessageReceived: TotemResponse? = null,
    val lastMessageSent: Message? = null,
    val snack: String? = null,
    val error: String? = null,
    val subscriptionMap: Map<String, Boolean>? = null
)

abstract class BaseTotemState {
    abstract val isPowerOn: Boolean
    abstract val isActive: Boolean
    abstract val topic: String
}

data class WaterPumpState(
    val payload: WaterPumpPayload? = null,
    override val isPowerOn: Boolean = false,
    override val isActive: Boolean = false,
    override val topic: String = ""
) : BaseTotemState()

data class MagSensorState(
    val payload: MagSensorPayload? = null,
    override val isPowerOn: Boolean = false,
    override val isActive: Boolean = false,
    override val topic: String = ""
) : BaseTotemState()