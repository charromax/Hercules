package com.example.hercules.domain.repository

import com.example.hercules.domain.models.Sensor
import kotlinx.coroutines.flow.Flow

interface SensorsRepository {
        fun getAllSensors(): Flow<List<Sensor>>
        suspend fun getSensorByID(id: Int): Sensor?
        suspend fun addNewSensor(sensor: Sensor)
        suspend fun deleteSensor(sensor: Sensor)
}