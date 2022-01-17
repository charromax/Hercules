package com.example.hercules.domain.repository

import com.example.hercules.data.local.SensorDao
import com.example.hercules.domain.models.Sensor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(private val dao: SensorDao): SensorsRepository {
    override fun getAllSensors(): Flow<List<Sensor>> {
        return dao.getAllSensors()
    }

    override suspend fun getSensorByID(id: Int): Sensor? {
        return dao.getSensorByID(id)
    }

    override suspend fun addNewSensor(sensor: Sensor) {
        dao.addNewSensor(sensor)
    }

    override suspend fun deleteSensor(sensor: Sensor) {
        dao.deleteSensor(sensor)
    }

}