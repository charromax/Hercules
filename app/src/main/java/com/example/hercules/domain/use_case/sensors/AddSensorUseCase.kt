/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.sensors

import com.example.hercules.data.model.DBSensor
import com.example.hercules.domain.model.InvalidSensorException
import com.example.hercules.data.repository.SensorsRepository
import javax.inject.Inject

class AddSensorUseCase @Inject constructor(
    private val repository: SensorsRepository
) {
    @Throws(InvalidSensorException::class)
    suspend operator fun invoke(sensor: DBSensor) {
        if (sensor.topic.isEmpty()) throw InvalidSensorException("El topic no puede estar vacío")
        if (sensor.topic.isBlank()) throw InvalidSensorException("El topic es inválido")
        repository.addNewSensor(sensor)
    }
}