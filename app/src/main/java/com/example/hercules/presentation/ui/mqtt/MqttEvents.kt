package com.example.hercules.presentation.ui.mqtt

import android.content.Context

sealed class MqttEvents {
    object StartConnectionRequest: MqttEvents()

    data class SubscribeToTopic(
        val topic: String
    ): MqttEvents()

    data class PublishMessage(
        val topic:String,
        val message: String
    ): MqttEvents()

}
