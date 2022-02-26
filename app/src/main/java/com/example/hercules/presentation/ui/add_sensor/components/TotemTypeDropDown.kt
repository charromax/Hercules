/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.add_sensor.components

import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import com.example.hercules.domain.model.TotemType
import java.lang.reflect.Modifier

@Composable
fun TotemTypeDropDown(
    onSelected: (TotemType) -> Unit,
    modifier: Modifier = Modifier(),
    isDDExpanded: Boolean = false,

    ) {
    DropdownMenu(expanded = isDDExpanded, onDismissRequest = { /*TODO*/ }) {

    }
}