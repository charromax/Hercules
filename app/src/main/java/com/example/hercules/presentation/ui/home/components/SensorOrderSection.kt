package com.example.hercules.presentation.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hercules.R
import com.example.hercules.presentation.utils.Order
import com.example.hercules.presentation.utils.SensorOrder

@Composable
fun SensorOrderSection(
    modifier: Modifier = Modifier,
    sensorOrder: SensorOrder = SensorOrder.Topic(Order.Ascending),
    onOrderChange: (SensorOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            HerculesRadioButton(
                text = stringResource(R.string.topic),
                selected = sensorOrder is SensorOrder.Topic,
                onSelect = {
                    onOrderChange(
                    SensorOrder.Topic(sensorOrder.order)
                    )
                })
            Spacer(modifier = Modifier.width(8.dp))
            HerculesRadioButton(
                text = stringResource(R.string.date),
                selected = sensorOrder is SensorOrder.Date,
                onSelect = {
                    onOrderChange(
                        SensorOrder.Date(sensorOrder.order)
                    )
                })
            Spacer(modifier = Modifier.width(8.dp))
            HerculesRadioButton(
                text = stringResource(R.string.state),
                selected = sensorOrder is SensorOrder.State,
                onSelect = {
                    onOrderChange(
                        SensorOrder.State(sensorOrder.order)
                    )
                })
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            HerculesRadioButton(
                text = "Ascendente",
                selected = sensorOrder.order is Order.Ascending,
                onSelect = {
                    onOrderChange(
                        sensorOrder.copy(Order.Ascending)
                    )
                })
            Spacer(modifier = Modifier.width(8.dp))
            HerculesRadioButton(
                text = "Descendente",
                selected = sensorOrder.order is Order.Descending,
                onSelect = {
                    onOrderChange(
                        sensorOrder.copy(Order.Descending)
                    )
                })
        }
    }

}