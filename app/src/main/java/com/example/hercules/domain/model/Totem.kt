/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.model

import com.example.hercules.R
import com.example.hercules.data.model.DBTotem

enum class PowerState {
    ON, OFF
}

enum class TotemType(val icon: Int) {
    WATER_PUMP(R.drawable.ic_water_pump),
    MAG_SENSOR(R.drawable.ic_door_sensor),
}

enum class BasicInstructionSet {
    ON,
    OFF,
    REPORT
}

abstract class Totem {
    abstract val id: Int
    abstract val topic: String
    abstract val powerState: PowerState
    abstract val name: String
    abstract val createdAt: Long
    abstract val isActive: Boolean
    abstract val type: TotemType

    abstract fun toDBObject(): DBTotem

    override fun toString(): String {
        return "$id - $topic"
    }
}
