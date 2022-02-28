/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.add_sensor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hercules.R
import com.example.hercules.domain.model.TotemType


@Composable
fun TotemTypeDropDownButton(
    onClick: () -> Unit,
    selectedTotemType: TotemType,
    onSelected: (TotemType) -> Unit,
    modifier: Modifier = Modifier,
    isDDExpanded: Boolean = false,
    onDismissRequested: () -> Unit
    ) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
            .padding(top = 8.dp)
            .background(shape = MaterialTheme.shapes.small, color = MaterialTheme.colors.background)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .clickable(onClick = onClick),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedTotemType.alias)
            Icon(
                painter = painterResource(id = R.drawable.ic_drop_down),
                contentDescription = null,
                modifier = Modifier.size(20.dp, 20.dp),
                tint = MaterialTheme.colors.onSurface
            )
            TotemTypeDropDown(
                isDDExpanded = isDDExpanded,
                onSelected = onSelected,
                onDismissRequested = onDismissRequested
            )
        }

    }
}