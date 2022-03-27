/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.model

import com.example.hercules.data.model.DBTotem
import com.example.hercules.data.remote.response.MqttManualParser
import com.example.hercules.data.remote.response.WaterPumpPayload

class Regador(
    override val id: Int,
    override val topic: String,
    override val powerState: Boolean,
    override val name: String,
    override val createdAt: Long,
    override val isActive: Boolean,
    override val type: TotemType,
    override val currentState: WaterPumpPayload?,
) : Totem() {

    override fun toDBObject(): DBTotem {
        return DBTotem(
            id = id,
            createdAt = createdAt,
            topic = topic,
            name = name,
            totemType = type
        )
    }

    companion object {
        fun build(totem: DBTotem) =
            Regador(
                id = totem.id,
                createdAt = totem.createdAt,
                topic = totem.topic,
                name = totem.name,
                type = totem.totemType,
                isActive = totem.isActive,
                powerState = totem.isPowerOn,
                currentState = MqttManualParser.buildPayload(
                    totem.rawJsonPayload,
                    totem.totemType
                ) as? WaterPumpPayload
            )
    }
}