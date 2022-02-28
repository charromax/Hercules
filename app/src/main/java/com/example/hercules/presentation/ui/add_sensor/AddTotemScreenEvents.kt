package com.example.hercules.presentation.ui.add_sensor

import com.example.hercules.data.model.DBTotem
import com.example.hercules.domain.model.TotemType

sealed class AddTotemScreenEvents {
    object OnDropDownDismissed: AddTotemScreenEvents()
    object OnDropDownOpen: AddTotemScreenEvents()
    data class OnTotemTypeSelected(val type: TotemType): AddTotemScreenEvents()
    object OnAddNewTotem: AddTotemScreenEvents()
}