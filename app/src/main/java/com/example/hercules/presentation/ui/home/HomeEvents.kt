package com.example.hercules.presentation.ui.home


import com.example.hercules.domain.model.Sensor
import com.example.hercules.presentation.utils.SensorOrder

sealed class HomeEvents {
    object OnToggleSectionOrder:HomeEvents()
    data class OnOrderChange(val sensorOrder: SensorOrder): HomeEvents()
    data class OnDeleteSensor(val sensor: Sensor): HomeEvents()
    object OnAddSensor: HomeEvents()
    object OnUndoDelete: HomeEvents()
}
