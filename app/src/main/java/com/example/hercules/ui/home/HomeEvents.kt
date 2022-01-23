package com.example.hercules.ui.home


import com.example.hercules.domain.models.Sensor
import com.example.hercules.utils.SensorOrder

sealed class HomeEvents {
    data class OnOrderChange(val sensorOrder: SensorOrder): HomeEvents()
    data class OnDeleteSensor(val sensor: Sensor): HomeEvents()
    object OnUndoDelete: HomeEvents()
}
