package com.example.hercules.ui.home

import com.example.hercules.domain.models.Sensor
import com.example.hercules.domain.utils.Order
import com.example.hercules.domain.utils.SensorOrder

data class HomeState(
    val sensors: List<Sensor> = emptyList(),
    val sensorOrder: SensorOrder = SensorOrder.Topic(Order.Ascending)
)
