/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.model

import com.example.hercules.R
import com.example.hercules.data.model.DBTotem
import com.example.hercules.data.remote.response.BaseTotemPayload
import com.example.hercules.presentation.ui.mqtt.BaseTotemState

enum class TotemType(val icon: Int, val alias: String) {
    WATER_PUMP(R.drawable.ic_water_pump, "Regador"),
    MAG_SENSOR(R.drawable.ic_door_sensor, "Sensor Magnetico"),
    AMBIENT_SENSOR(R.drawable.ic_door_sensor, "Sensor Ambiental"),
}

enum class BasicInstructionSet {
    ON,
    OFF,
    REPORT
}

abstract class Totem {
    abstract val id: Int
    abstract val topic: String
    abstract val powerState: Boolean
    abstract val name: String
    abstract val createdAt: Long
    abstract val isActive: Boolean
    abstract val type: TotemType
    abstract val currentState: BaseTotemPayload?

    abstract fun toDBObject(): DBTotem

    override fun toString(): String {
        return "$id - $topic"
    }
}

fun Totem.isThisMyPayload(state: BaseTotemState): Boolean {
    if (state.topic != topic) return false
    return true
}
