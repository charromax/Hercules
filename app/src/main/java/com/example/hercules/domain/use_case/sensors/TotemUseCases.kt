/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.sensors

data class TotemUseCases(
    val getAllSensorsUseCase: GetAllSensorsUseCase,
    val deleteSensorUseCase: DeleteSensorUseCase,
    val saveNewSensor: AddSensorUseCase,
    val updateTotemUseCase: UpdateTotemUseCase,
    val getTotemByIdUseCase: GetTotemByIdUseCase,
    val getTotemByTopicUseCase: GetTotemByTopicUseCase
)
