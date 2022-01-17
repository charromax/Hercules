package com.example.hercules.domain.use_case

import com.example.hercules.domain.models.InvalidSensorException
import com.example.hercules.domain.models.Sensor
import com.example.hercules.domain.repository.SensorsRepository
import javax.inject.Inject

class AddSensorUseCase @Inject constructor(
    private val repository: SensorsRepository
) {

    @Throws(InvalidSensorException::class)
    suspend operator fun invoke(sensor: Sensor) {
        if (sensor.topic.isEmpty()) throw InvalidSensorException("El topic no puede estar vacío")
        if (sensor.topic.isBlank()) throw InvalidSensorException("El topic es inválido")
        repository.addNewSensor(sensor)
    }

}