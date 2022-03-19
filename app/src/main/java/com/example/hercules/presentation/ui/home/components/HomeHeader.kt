/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hercules.presentation.ui.components.TopBarStandard
import com.example.hercules.presentation.utils.SensorOrder

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    isOrderSectionVisible: Boolean,
    order: SensorOrder,
    headerTitle: String,
    onToggleOrderButton: () -> Unit,
    onOrderChange: (SensorOrder) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TopBarStandard(
            isToggleOrderButtonVisible = true,
            headerTitle = headerTitle,
            onToggleOrderButton = onToggleOrderButton
        )
        AnimatedVisibility(
            visible = isOrderSectionVisible,
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