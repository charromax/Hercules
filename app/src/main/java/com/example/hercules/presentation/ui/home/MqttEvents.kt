package com.example.hercules.presentation.ui.home

import android.content.Context

sealed class MqttEvents {
    data class StartConnectionRequest(
        val context: Context,
        val topics:List<String>
    ): MqttEvents()
}
