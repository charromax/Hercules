/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home.components.totems

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
import androidx.compose.ui.unit.dp
import com.example.hercules.R
import com.example.hercules.data.remote.response.MagSensorPayload
import com.example.hercules.domain.model.MagSensor
import com.example.hercules.domain.model.Totem
import com.example.hercules.presentation.ui.components.BaseTotemCard

const val TAG = "magnetic_sensor"

@Composable
fun MagneticSensor(
    magSensor: MagSensor,
    modifier: Modifier = Modifier,
    onButtonClicked: (totem: Totem) -> Unit,
    onDeleteButtonClicked: (totem: Totem) -> Unit
) {
    val colorActive = MaterialTheme.colors.primaryVariant

    BaseTotemCard {
        Column(
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
                    text = magSensor.topic,
                    style = MaterialTheme.typography.h6,
                    color = if (magSensor.isActive) colorActive else MaterialTheme.colors.onSurface,
                    overflow = TextOverflow.Ellipsis
                )

                Image(
                    modifier = modifier
                        .clickable {
                            onDeleteButtonClicked.invoke(magSensor)
                        },
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null
                )

            }
            Text(
                text = checkIsActivated(magSensor.isActive),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface,
                modifier = modifier.padding(horizontal = 8.dp)
            )
            Button(
                onClick = {
                    onButtonClicked(magSensor)
                },
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        color = getSensorButtonColor(
                            magSensor.isActive,
                            magSensor.powerState, magSensor.currentState?.data
                        ),
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Text(
                    text = checkSensorPayload(magSensor.currentState),
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }

}

@Composable
fun checkSensorPayload(magSensorPayload: MagSensorPayload?): String {
    return if (magSensorPayload?.data == true) stringResource(R.string.alarm) else stringResource(R.string.ok)
}

@Composable
private fun getSensorButtonColor(
    activation: Boolean,
    power: Boolean,
    isTriggered: Boolean?
): Color {
    return when {
        activation && power && isTriggered == true -> MaterialTheme.colors.error
        activation && isTriggered == false -> Color.Green
        !activation || !power -> Color.Gray
        else -> MaterialTheme.colors.primary
    }
}

@Composable
private fun checkIsActivated(activation: Boolean) =
    if (activation) stringResource(R.string.sensor_activated) else stringResource(
        R.string.sensor_deactivated
    )
