package com.example.hercules.domain.use_case

import com.example.hercules.domain.models.Sensor
import com.example.hercules.domain.repository.SensorsRepository
import javax.inject.Inject

class DeleteSensorUseCase @Inject constructor(
    private val repo:SensorsRepository
) {

    suspend operator fun invoke(sensor:Sensor) {
        repo.deleteSensor(sensor)
    }
}