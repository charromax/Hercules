/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hercules.domain.models.Sensor
import org.joda.time.Instant

@Entity(tableName = "sensors")
data class DBSensor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val createdAt: Long = Instant.now().millis,
    val topic: String,
) {
    /**
     * maps to domain-defined Sensor model
     */
    fun toSensor(): Sensor {
        return Sensor(
            id = id,
            createdAt = createdAt,
            topic = topic,
            state = false,
            isActive = false
        )
    }

}
