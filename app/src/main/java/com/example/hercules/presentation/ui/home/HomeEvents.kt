package com.example.hercules.presentation.ui.home


import com.example.hercules.data.model.DBTotem
import com.example.hercules.domain.model.Totem
import com.example.hercules.presentation.utils.SensorOrder

sealed class HomeEvents {
    object OnToggleSectionOrder:HomeEvents()
    data class OnOrderChange(val sensorOrder: SensorOrder): HomeEvents()
    data class OnDeleteTotem(val totem: Totem): HomeEvents()
    data class OnAddTotem(val totem: DBTotem): HomeEvents()
    object OnUndoDelete: HomeEvents()
}
