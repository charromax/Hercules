/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.sensors

import com.example.hercules.domain.model.Sensor
import com.example.hercules.data.repository.SensorsRepository
import javax.inject.Inject

class DeleteSensorUseCase @Inject constructor(
    private val repo: SensorsRepository
) {
    suspend operator fun invoke(sensor:Sensor) {
        repo.deleteSensor(sensor.toDBSensor())
    }
}