/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.add_sensor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hercules.domain.model.TotemType

@Composable
fun TotemTypeDropDown(
    onSelected: (TotemType) -> Unit,
    isDDExpanded: Boolean = false,
    onDismissRequested: () -> Unit
) {
    val listOfTypes = TotemType.values()
    DropdownMenu(
        expanded = isDDExpanded,
        onDismissRequest = onDismissRequested,
        modifier = Modifier.fillMaxWidth(),
    ) {
        listOfTypes.forEach { totemType ->
            DropdownMenuItem(
                onClick = { onSelected(totemType) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = totemType.icon),
                        contentDescription = totemType.name,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(text = totemType.alias, color = MaterialTheme.colors.onSurface)
                }

            }
        }
    }
}