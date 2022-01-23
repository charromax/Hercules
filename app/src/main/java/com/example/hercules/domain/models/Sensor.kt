package com.example.hercules.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.Instant

@Entity(tableName = "sensors")
data class Sensor(
    @PrimaryKey val id: Int,
    val createdAt: Long = Instant.now().millis,
    val topic: String,
    val state: Boolean,
    val isActive: Boolean
)

class InvalidSensorException(message: String) : Exception(message)