/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hercules.domain.model.MagSensor
import com.example.hercules.domain.model.Regador
import com.example.hercules.domain.model.Totem
import com.example.hercules.domain.model.TotemType
import org.joda.time.DateTime

@Entity(tableName = "totems")
data class DBTotem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = DateTime.now().millis,
    val topic: String,
    val name: String,
    @ColumnInfo(name = "totem_type")
    val totemType: TotemType,
    @ColumnInfo(name = "is_power_on")
    val isPowerOn: Boolean = false,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = false,
    @ColumnInfo(name = "json_payload")
    val rawJsonPayload: String = ""
) {
    fun toTotem(): Totem {
        return when (totemType) {
            TotemType.WATER_PUMP -> Regador.build(this)
            TotemType.MAG_SENSOR -> MagSensor.build(this)
            TotemType.AMBIENT_SENSOR -> TODO()
        }
    }
}

