/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hercules.R

@Composable
fun TopBarStandard(
    isToggleOrderButtonVisible: Boolean = false,
    isBackButtonVisible: Boolean = false,
    headerTitle: String,
    onToggleOrderButton: (() -> Unit)? = null,
    onBackButtonPressed: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .padding(8.dp),
        horizontalArrangement = if (isBackButtonVisible) Arrangement.Start else Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isBackButtonVisible) {
            IconButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = { onBackButtonPressed?.invoke() },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
        Text(
            text = headerTitle,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onPrimary
        )
        if (isToggleOrderButtonVisible) {
            IconButton(
                onClick = { onToggleOrderButton?.invoke() },
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
    }
}