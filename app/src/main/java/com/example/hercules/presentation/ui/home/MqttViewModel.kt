/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hercules.domain.model.Message
import com.example.hercules.domain.use_case.mqtt.MqttUseCases
import com.example.hercules.presentation.utils.Failure
import com.example.hercules.presentation.utils.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MqttViewModel @Inject constructor(
    private val mqttUseCases: MqttUseCases
) : ViewModel() {

    private val _mqttState = MutableStateFlow(MqttState())
    val mqttState: StateFlow<MqttState> = _mqttState

    fun onEvent(event: MqttEvents) {
        when (event) {
            is MqttEvents.StartConnectionRequest -> connect(event.context, event.topics)
        }
    }

    /**
     * connect to mqtt broker
     */
    private fun connect(context: Context, topics: List<String>) {
        viewModelScope.launch {
            when (val result = mqttUseCases.connectMqttUseCase(context, topics)) {
                is Failure -> updateErrorState(result.error)
                is Success -> {
                    _mqttState.value =
                        mqttState.value.copy(isMqttConnected = true, snack = result.value)
                }
            }
        }
    }

    /**
     * disconnect from mqtt broker
     */
    private fun disconnect() {
        viewModelScope.launch {
            when (val result = mqttUseCases.disconnectMqttUseCase()) {
                is Failure -> updateErrorState(result.error)
                is Success -> _mqttState.value =
                    mqttState.value.copy(isMqttConnected = false, snack = result.value)
            }
        }
    }

    private fun getMessages() {
        viewModelScope.launch {
            when (val result = mqttUseCases.getMessageUseCase()) {
                is Failure -> updateErrorState(result.error)
                is Success -> _mqttState.value = mqttState.value.copy(
                    lastMessageReceived = result.value
                )
            }
        }
    }

    private fun publish(topic: String, data: String) {
        viewModelScope.launch {
            when (val result = mqttUseCases.publishMqttUseCase(topic, data)) {
                is Failure -> updateErrorState(result.error)
                is Success -> _mqttState.value = mqttState.value.copy(
                    lastMessageSent = Message(topic = topic, message = data)
                )
            }
        }
    }

    private fun updateErrorState(e: Exception) {
        _mqttState.value = mqttState.value.copy(
            error = e
        )
    }
}
