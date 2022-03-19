/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.hercules.presentation.theme.HerculesTheme
import com.example.hercules.presentation.ui.mqtt.MqttEvents
import com.example.hercules.presentation.ui.mqtt.MqttViewModel
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "MAIN"

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mqttViewModel: MqttViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mqttViewModel.onEvent(MqttEvents.StartConnectionRequest)
        setContent {
            HerculesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Navigation()
                }
            }
        }
    }

}

