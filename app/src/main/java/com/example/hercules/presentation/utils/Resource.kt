/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.utils

import org.eclipse.paho.client.mqttv3.MqttException

sealed class Resource<out R> {
    data class Success(val message: String) : Resource<Nothing>()
    data class Failure(val exception: Throwable?, val message: String?) : Resource<Nothing>()
    data class Error(val exception: MqttException, val message: String?) : Resource<Nothing>()
    data class UnknownError(val exception: Throwable, val message: String?) : Resource<Nothing>()
    data class ConnectionLost(val exception: Throwable?) : Resource<Nothing>()
    data class MessageReceived<out T>(val data: T) : Resource<T>()
}