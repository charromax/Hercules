/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.repository

import com.example.hercules.data.local.SensorDao
import com.example.hercules.data.model.DBSensor
import com.example.hercules.domain.models.Sensor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(private val dao: SensorDao): SensorsRepository {
    override fun getAllSensors(): Flow<List<Sensor>> {
        return dao.getAllSensors().map { list ->
            list.map { it.toSensor() }
        }
    }

    override suspend fun getSensorByID(id: Int): Sensor? {
        return dao.getSensorByID(id)?.toSensor()
    }

    override suspend fun addNewSensor(sensor: DBSensor) {
        dao.addNewSensor(sensor)
    }

    override suspend fun deleteSensor(sensor: DBSensor) {
        dao.deleteSensor(sensor)
    }

}