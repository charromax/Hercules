package com.example.hercules.presentation.ui.home

import com.example.hercules.domain.model.Sensor
import com.example.hercules.presentation.utils.Order
import com.example.hercules.presentation.utils.SensorOrder

data class HomeState(
    val sensors: List<Sensor> = emptyList(),
    val sensorOrder: SensorOrder = SensorOrder.Topic(Order.Ascending),
    val navigateToAddSensorScreen: Boolean = false,
    val isOrderSectionVisible: Boolean = false
)
