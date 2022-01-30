/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.model

import com.example.hercules.data.model.DBSensor


data class Sensor(
    val id: Int,
    val createdAt: Long,
    val topic: String,
    val state: Boolean,
    val isActive: Boolean
) {
    override fun toString(): String {
        return "$id - $topic"
    }

    fun toDBSensor(): DBSensor {
        return DBSensor(
            id = id,
            createdAt = createdAt,
            topic = topic
        )
    }
}

class InvalidSensorException(message: String) : Exception(message)