/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.ui.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hercules.domain.models.Sensor
import com.example.hercules.ui.theme.MediumGray

@Composable
fun SensorListItem(
    sensor: Sensor,
    onPowerButtonClicked: (sensor: Sensor) -> Unit,
) {
    val colorActive = MaterialTheme.colors.primaryVariant
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = sensor.topic,
            style = MaterialTheme.typography.h5,
            color = if (sensor.isActive) colorActive else MediumGray
        )
    }
}