/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.model

import com.example.hercules.data.model.DBTotem
import com.example.hercules.data.remote.response.MagSensorPayload
import com.example.hercules.data.remote.response.MqttManualParser

class MagSensor(
    val data: Boolean,
    override val id: Int,
    override val topic: String,
    override val powerState: Boolean,
    override val name: String,
    override val createdAt: Long,
    override val isActive: Boolean = false,
    override val type: TotemType,
    override val currentState: MagSensorPayload?
) : Totem() {

    override fun toDBObject(): DBTotem {
        return DBTotem(
            topic = this.topic,
            name = this.name,
            totemType = this.type,
        )
    }

    companion object {
        fun build(totem: DBTotem) =
            MagSensor(
                id = totem.id,
                createdAt = totem.createdAt,
                topic = totem.topic,
                data = false,
                powerState = totem.isPowerOn,
                isActive = totem.isActive,
                name = totem.name,
                type = totem.totemType,
                currentState = MqttManualParser.buildPayload(
                    totem.rawJsonPayload,
                    totem.totemType
                ) as? MagSensorPayload
            )
    }
}

class InvalidSensorException(message: String) : Exception(message)