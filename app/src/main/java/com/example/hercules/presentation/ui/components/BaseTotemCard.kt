/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun BaseTotemCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val shape = MaterialTheme.shapes.small
    Column(
        modifier = modifier
            .padding(8.dp)
            .border(
                shape = shape,
                width = 1.dp,
                color = MaterialTheme.colors.primary
            )
            .clip(shape)
            .background(
                color = MaterialTheme.colors.surface,
            ),
        horizontalAlignment = Alignment.Start,
    ) {
        content()
    }
}