/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.mqtt

sealed class MqttEvents {
    object StartConnectionRequest : MqttEvents()

    data class SubscribeToTopic(
        val topic: String
    ) : MqttEvents()

    object RefreshSubscription : MqttEvents()

    data class PublishMessage(
        val topic: String,
        val message: String
    ) : MqttEvents()

}
