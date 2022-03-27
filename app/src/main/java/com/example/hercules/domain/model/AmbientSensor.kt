/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.model

import com.example.hercules.data.model.DBTotem
import com.example.hercules.data.remote.response.AmbientSensorPayload
import com.example.hercules.data.remote.response.BaseTotemPayload
import com.example.hercules.data.remote.response.MqttManualParser

data class AmbientSensor(
    override val id: Int,
    override val topic: String,
    override val powerState: Boolean,
    override val name: String,
    override val createdAt: Long,
    override val isActive: Boolean,
    override val type: TotemType,
    override val currentState: BaseTotemPayload?
): Totem() {
    override fun toDBObject(): DBTotem {
        return DBTotem(
            id = id,
            topic = topic,
            name = name,
            totemType = type
        )
    }

    companion object {
        fun build(totem: DBTotem) =
            AmbientSensor(
                id = totem.id,
                createdAt = totem.createdAt,
                topic = totem.topic,
                name = totem.name,
                type = totem.totemType,
                isActive = false,
                powerState = false,
                currentState = MqttManualParser.buildPayload(
                    totem.rawJsonPayload,
                    totem.totemType
                ) as? AmbientSensorPayload
            )
    }
}