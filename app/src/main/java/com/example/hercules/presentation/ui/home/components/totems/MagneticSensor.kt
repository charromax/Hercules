package com.example.hercules.presentation.ui.home.components.totems

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hercules.R
import com.example.hercules.domain.model.Message
import com.example.hercules.domain.model.Sensor
import com.example.hercules.domain.model.Totem
import com.example.hercules.domain.model.TotemType
import org.joda.time.DateTime

const val TAG = "magnetic_sensor"

@Composable
fun MagneticSensor(
    sensor: Sensor,
    modifier: Modifier = Modifier,
    onButtonClicked: (totem: Totem) -> Unit,
    onDeleteButtonClicked: (totem: Totem) -> Unit,
    lastMessageReceived: Message?
) {
    val colorActive = MaterialTheme.colors.primaryVariant
    Column(
        modifier = modifier
            .padding(8.dp)
            .background(color = MaterialTheme.colors.surface, shape = MaterialTheme.shapes.small),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = sensor.topic,
                style = MaterialTheme.typography.h6,
                color = if (sensor.isActive) colorActive else MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis
            )

            Image(
                modifier = modifier
                    .clickable {
                        onDeleteButtonClicked.invoke(sensor)
                    },
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null
            )

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
                    color = getSensorButtonColor(sensor),
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Text(text = getAlarmText(sensor, lastMessageReceived))
        }
    }
}

@Composable
fun getAlarmText(sensor: Sensor, lastMessageReceived: Message?): String {
    return when {
        (lastMessageReceived != null && sensor.topic == lastMessageReceived.topic) -> lastMessageReceived.message
        else -> ""
    }
}

@Composable
private fun getSensorButtonColor(sensor: Sensor): Color {
    return when {
        sensor.isActive && sensor.isTriggered -> MaterialTheme.colors.error
        sensor.isActive && !sensor.isTriggered -> Color.Green
        else -> MaterialTheme.colors.primary
    }
}

@Composable
private fun checkIsActivated(sensor: Sensor) =
    if (sensor.isActive) stringResource(R.string.sensor_activated) else stringResource(
        R.string.sensor_deactivated
    )
