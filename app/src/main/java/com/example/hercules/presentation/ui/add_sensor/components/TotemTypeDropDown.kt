/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.add_sensor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.hercules.domain.model.TotemType

@Composable
fun TotemTypeDropDown(
    onSelected: (TotemType) -> Unit,
    modifier: Modifier = Modifier,
    isDDExpanded: Boolean = false,
    onDismissRequested: () -> Unit
) {
    val listOfTypes = TotemType.values()
    DropdownMenu(
        expanded = isDDExpanded,
        onDismissRequest = onDismissRequested,
        modifier = modifier.fillMaxWidth(),
    ) {
        listOfTypes.forEach { totemType ->
            DropdownMenuItem(
                onClick = { onSelected(totemType) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = totemType.icon),
                        contentDescription = totemType.name
                    )
                    Text(text = totemType.name)
                }

            }
        }
    }
}