/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.ui.home.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hercules.R
import com.example.hercules.domain.models.Sensor
import org.joda.time.DateTime

const val TAG = "sensor"

@Composable
fun SensorListItem(
    sensor: Sensor,
    modifier: Modifier = Modifier,
    onButtonClicked: (sensor: Sensor) -> Unit,
    onDeleteButtonClicked: (sensor: Sensor) -> Unit
) {
    val colorActive = MaterialTheme.colors.primaryVariant
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .padding(8.dp)
            .background(color = MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = sensor.topic,
            style = MaterialTheme.typography.h5,
            color = if (sensor.isActive) colorActive else MaterialTheme.colors.onSurface
        )
        Text(
            text = checkIsActivated(sensor),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface
        )
        Button(
            onClick = {
                onButtonClicked(sensor)
            },
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = getSensorButtonColor(sensor = sensor),
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Text(text = getAlarmText(sensor))
        }
    }
}

@Composable
fun getAlarmText(sensor: Sensor): String {
    return when {
        sensor.isActive && sensor.state -> "ALARMA"
        else -> ""
    }
}

@Composable
private fun getSensorButtonColor(sensor: Sensor): Color {
    return when {
        sensor.isActive && sensor.state -> MaterialTheme.colors.error
        sensor.isActive && !sensor.state -> Color.Green
        else -> Color.LightGray
    }
}

@Composable
private fun checkIsActivated(sensor: Sensor) =
    if (sensor.isActive) stringResource(R.string.sensor_activated) else stringResource(
        R.string.sensor_deactivated
    )

@Preview
@Composable
fun SensorItemPreview() {
    val sensor = Sensor(
        topic = "home/office/door",
        state = true,
        isActive = true,
        id = 0,
        createdAt = DateTime.now().millis
    )
    
    SensorListItem(sensor = sensor, onButtonClicked = {
        Log.i(TAG, "SensorItemPreview $sensor")
    },
        onDeleteButtonClicked = {
            Log.i(TAG, "SensorItemPreview: onDelete")
        })
}
