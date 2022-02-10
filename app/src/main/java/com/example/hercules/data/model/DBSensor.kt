/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hercules.domain.model.Sensor
import org.joda.time.Instant

@Entity(tableName = "sensors")
data class DBSensor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: Long = Instant.now().millis,
    val topic: String,
    val name: String
) {
    /**
     * maps to domain-defined Sensor model
     */
    fun toSensor(): Sensor {
        return Sensor(
            id = id,
            createdAt = createdAt,
            topic = topic,
            isTriggered = false,
            name = name
        )
    }

}
