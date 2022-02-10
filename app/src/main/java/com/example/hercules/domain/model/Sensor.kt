/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.model

import com.example.hercules.data.model.DBSensor


class Sensor(
    val isTriggered: Boolean,
    override val id: Int,
    override val topic: String,
    override val powerState: PowerState = PowerState.OFF,
    override val name: String,
    override val createdAt: Long,
    override val isActive: Boolean = false
) : Totem() {

    fun toDBSensor(): DBSensor {
        return DBSensor(
            id = id,
            createdAt = createdAt,
            topic = topic,
            name = name
        )
    }

    override fun toString(): String {
        return "$id - $topic"
    }
}

class InvalidSensorException(message: String) : Exception(message)