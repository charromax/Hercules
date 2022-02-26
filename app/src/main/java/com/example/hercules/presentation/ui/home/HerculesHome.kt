/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hercules.R
import com.example.hercules.presentation.ui.mqtt.MqttViewModel

@Composable
fun HerculesHome(
    sensorViewModel: TotemsViewModel = viewModel(),
    mqttViewModel: MqttViewModel
) {
    val state = sensorViewModel.homeState.collectAsState()
    val scope = rememberCoroutineScope()
    val deleteMsg = stringResource(R.string.sensor_deleted)
    val undoLabel = stringResource(R.string.undo)

    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.background
            )
            .fillMaxSize()
    ) {
        Column {

        }
    }
}

