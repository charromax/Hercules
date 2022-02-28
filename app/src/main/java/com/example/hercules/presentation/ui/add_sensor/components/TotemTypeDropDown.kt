/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.add_sensor.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hercules.domain.model.TotemType

@Composable
fun TotemTypeDropDown(
    onSelected: (TotemType) -> Unit,
    modifier: Modifier = Modifier,
    isDDExpanded: Boolean = false,
    onDismissRequested: () -> Unit
) {
    DropdownMenu(
        expanded = isDDExpanded,
        onDismissRequest = onDismissRequested,
        modifier = modifier.fillMaxWidth(),
    ) {

    }
}