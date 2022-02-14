/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hercules.domain.model.Regador
import com.example.hercules.domain.model.Sensor
import com.example.hercules.domain.model.Totem
import com.example.hercules.domain.model.TotemType
import org.joda.time.Instant

@Entity(tableName = "sensors")
data class DBTotem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: Long = Instant.now().millis,
    val topic: String,
    val name: String,
    @ColumnInfo(name = "totem_type") val totemType: TotemType
) {
    /**
     * maps to domain-defined Sensor model
     */
    fun toTotem(): Totem {
        return when (totemType) {
            TotemType.WATER_PUMP -> Regador.build(this)
            TotemType.MAG_SENSOR -> Sensor.build(this)
        }

    }

}
