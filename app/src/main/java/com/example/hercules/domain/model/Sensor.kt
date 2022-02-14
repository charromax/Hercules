/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.model

import com.example.hercules.data.model.DBTotem


class Sensor(
    val isTriggered: Boolean,
    override val id: Int,
    override val topic: String,
    override val powerState: PowerState = PowerState.OFF,
    override val name: String,
    override val createdAt: Long,
    override val isActive: Boolean = false,
    override val type: TotemType
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
            Sensor(
                id = totem.id,
                createdAt = totem.createdAt,
                topic = totem.topic,
                isTriggered = false,
                name = totem.name,
                type = totem.totemType
            )
    }
}

class InvalidSensorException(message: String) : Exception(message)