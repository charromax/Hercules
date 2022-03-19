/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.add_sensor

import com.example.hercules.domain.model.TotemType

data class AddTotemScreenState(
    val snack: String? = null,
    val error: String? = null,
    val isDropDownExpanded: Boolean = false,
    val selectedTotemType: TotemType = TotemType.MAG_SENSOR,
    val isSaveSuccessful: Boolean = false,
    val newTotemName: String? = null,
    val newTotemTopic: String? = null
)
