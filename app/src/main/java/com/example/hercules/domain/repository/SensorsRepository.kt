/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.repository

import com.example.hercules.data.model.DBSensor
import com.example.hercules.domain.models.Sensor
import kotlinx.coroutines.flow.Flow

interface SensorsRepository {
    fun getAllSensors(): Flow<List<Sensor>>
    suspend fun getSensorByID(id: Int): Sensor?
    suspend fun addNewSensor(sensor: DBSensor)
    suspend fun deleteSensor(sensor: DBSensor)
}