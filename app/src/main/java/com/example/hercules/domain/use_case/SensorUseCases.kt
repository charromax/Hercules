package com.example.hercules.domain.use_case

data class SensorUseCases(
    val getAllSensorsUseCase: GetAllSensorsUseCase,
    val deleteSensorUseCase: DeleteSensorUseCase,
    val saveNewSensor: AddSensorUseCase
)
