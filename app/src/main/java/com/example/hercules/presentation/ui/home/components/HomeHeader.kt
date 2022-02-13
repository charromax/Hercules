package com.example.hercules.presentation.ui.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hercules.R
import com.example.hercules.presentation.utils.SensorOrder

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    order: SensorOrder,
    headerTitle: @Composable () -> Unit,
    onToggleOrderButton: () -> Unit,
    onOrderChange: (SensorOrder) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.primary)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            headerTitle()
            IconButton(
                onClick = onToggleOrderButton,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sort),
                    contentDescription = stringResource(
                        R.string.sort_sensors
                    ),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = expandVertically(),
            exit = shrinkVertically(),

            ) {
            SensorOrderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                sensorOrder = order,
                onOrderChange = {
                    onOrderChange(it)
                })
        }
    }
}