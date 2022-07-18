/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home.components.totems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.hercules.R
import com.example.hercules.domain.model.Regador
import com.example.hercules.domain.model.Totem
import com.example.hercules.presentation.ui.components.BaseTotemCard


@Composable
fun WaterPumpListItem(
    regador: Regador,
    onButtonClicked: (totem: Totem) -> Unit,
    onRefreshButtonClicked: (totem: Totem) -> Unit,
    onDeleteButtonClicked: (totem: Totem) -> Unit
) {
    val colorActive = MaterialTheme.colors.primaryVariant
    BaseTotemCard {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onDeleteButtonClicked(regador)
                        },
                    imageVector = Icons.Default.Delete,
                    tint = Color.Red,
                    contentDescription = "delete button"
                )
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onRefreshButtonClicked(regador)
                        },
                    imageVector = Icons.Default.Refresh,
                    tint = MaterialTheme.colors.onSurface,
                    contentDescription = "refresh button"
                )
            }
            Text(
                text = regador.name,
                style = MaterialTheme.typography.h6,
                color = if (regador.isActive) colorActive
                else MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = checkIsActivated(regador),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface,
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                        .padding(4.dp)
                        .clickable {
                            onButtonClicked(regador)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

        }
    }
}

@Composable
private fun getButtonText(regador: Regador): String {
    return if (regador.currentState?.isWorking == true) stringResource(R.string.watering_in_progress)
    else stringResource(R.string.pump_water)
}

@Composable
private fun checkIsActivated(regador: Regador): String {
    return when {
        regador.powerState && regador.isActive -> stringResource(R.string.sensor_activated)
        !regador.powerState -> stringResource(R.string.totem_shut_down)
        else -> stringResource(
            R.string.sensor_deactivated
        )
    }
}

