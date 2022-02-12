/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hercules.R
import com.example.hercules.domain.model.Sensor
import com.example.hercules.presentation.ui.home.HomeEvents
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
            .padding(8.dp)
            .background(color = MaterialTheme.colors.surface, shape = MaterialTheme.shapes.small),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = sensor.topic,
                style = MaterialTheme.typography.h6,
                color = if (sensor.isActive) colorActive else MaterialTheme.colors.onSurface,
                modifier = modifier.padding(8.dp)
            )
            IconButton(
                modifier = modifier.padding(4.dp),
                onClick = { onDeleteButtonClicked(sensor) },
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_delete),
                    contentDescription = null
                )
            }
        }
        Text(
            text = checkIsActivated(sensor),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
                    modifier = modifier.padding(horizontal = 8.dp)
        )
        Button(
            onClick = {
                onButtonClicked(sensor)
            },
            modifier = modifier
                .padding(8.dp)
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
        sensor.isActive && sensor.isTriggered -> "ALARMA"
        else -> ""
    }
}

@Composable
private fun getSensorButtonColor(sensor: Sensor): Color {
    return when {
        sensor.isActive && sensor.isTriggered -> MaterialTheme.colors.error
        sensor.isActive && !sensor.isTriggered -> Color.Green
        else -> MaterialTheme.colors.onSurface
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
        isTriggered = true,
        isActive = true,
        id = 0,
        createdAt = DateTime.now().millis,
        name = "sensor oficina"
    )
    
    SensorListItem(sensor = sensor, onButtonClicked = {
        Log.i(TAG, "SensorItemPreview $sensor")
    },
        onDeleteButtonClicked = {
            Log.i(TAG, "SensorItemPreview: onDelete")
        })
}
