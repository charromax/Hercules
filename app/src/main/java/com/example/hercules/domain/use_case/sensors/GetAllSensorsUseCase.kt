/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.sensors


import com.example.hercules.data.repository.TotemRepository
import com.example.hercules.domain.model.MagSensor
import com.example.hercules.domain.model.Totem
import com.example.hercules.presentation.utils.Order
import com.example.hercules.presentation.utils.SensorOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllSensorsUseCase @Inject constructor(
    private val repo: TotemRepository
) {

    operator fun invoke(sensorOrder: SensorOrder = SensorOrder.Topic(Order.Ascending)): Flow<List<Totem>> {
        return repo.getAllTotems().map { sensors ->
            when (sensorOrder.order) {
                Order.Ascending -> {
                    when (sensorOrder) {
                        is SensorOrder.Date -> sensors.sortedBy { it.createdAt }
                        is SensorOrder.Topic -> sensors.sortedBy { it.topic }
                        is SensorOrder.State -> sensors.sortedBy {
                            when (it) {
                                is MagSensor -> it.data
                                else -> it.isActive
                            }
                        }
                    }
                }
                Order.Descending -> {
                    when (sensorOrder) {
                        is SensorOrder.Date -> sensors.sortedByDescending { it.createdAt }
                        is SensorOrder.Topic -> sensors.sortedByDescending { it.topic }
                        is SensorOrder.State -> sensors.sortedByDescending {
                            when (it) {
                                is MagSensor -> it.data
                                else -> it.isActive
                            }
                        }
                    }
                }
            }
        }
    }
}