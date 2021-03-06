/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hercules.R
import com.example.hercules.domain.model.Regador
import com.example.hercules.domain.model.Totem
import com.example.hercules.domain.model.TotemType
import com.example.hercules.presentation.ui.components.BaseTotemCard
import org.joda.time.DateTime

const val TAG = "regador"

@Composable
fun WaterPumpListItem(
    regador: Regador,
    modifier: Modifier = Modifier,
    onButtonClicked: (totem: Totem) -> Unit,
    onRefreshButtonClicked: (totem: Totem) -> Unit,
    onDeleteButtonClicked: (totem: Totem) -> Unit
) {
    val colorActive = MaterialTheme.colors.primaryVariant
    BaseTotemCard {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = regador.topic,
                    style = MaterialTheme.typography.h6,
                    color = if (regador.isActive) colorActive else MaterialTheme.colors.onSurface,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = modifier
                            .clickable {
                                onDeleteButtonClicked.invoke(regador)
                            },
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                    )

                    Image(
                        modifier = modifier
                            .clickable {
                                onDeleteButtonClicked.invoke(regador)
                            },
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null
                    )
                }
            }
            Text(
                text = checkIsActivated(regador),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface,
                modifier = modifier.padding(horizontal = 8.dp)
            )
            Button(
                onClick = {
                    onButtonClicked(regador)
                },
                modifier = modifier
                    .padding(8.dp)
                    .height(50.dp)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Text(
                    text = stringResource(R.string.pump_water),
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }

}

@Composable
private fun checkIsActivated(regador: Regador) =
    if (regador.isActive) stringResource(R.string.sensor_activated) else stringResource(
        R.string.sensor_deactivated
    )

@Preview
@Composable
fun SensorItemPreview() {
    val regador = Regador(
        topic = "home/terrace/pump",
        isActive = true,
        id = 0,
        createdAt = DateTime.now().millis,
        name = "sensor oficina",
        type = TotemType.MAG_SENSOR,
        powerState = true
    )

    WaterPumpListItem(regador = regador, onButtonClicked = {
        Log.i(TAG, "SensorItemPreview $regador")
    },
        onDeleteButtonClicked = {
            Log.i(TAG, "SensorItemPreview: onDelete")
        },
        onRefreshButtonClicked = {
            Log.i(TAG, "SensorItemPreview: on Refresh")
        })
}
