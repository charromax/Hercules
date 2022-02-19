/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.mqtt

import androidx.compose.animation.core.updateTransition
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hercules.domain.model.Message
import com.example.hercules.domain.use_case.mqtt.MqttUseCase
import com.example.hercules.presentation.utils.Failure
import com.example.hercules.presentation.utils.Resource
import com.example.hercules.presentation.utils.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MqttViewModel @Inject constructor(
    private val mqttUseCase: MqttUseCase
) : ViewModel() {
    private var connectionMqttJob: Job? = null
    private val _mqttState = MutableStateFlow(MqttState())
    val mqttState: StateFlow<MqttState> = _mqttState
    val topicList = mutableListOf<String>()

    fun onEvent(event: MqttEvents) {
        when (event) {
            MqttEvents.StartConnectionRequest -> connect()
            is MqttEvents.PublishMessage -> publish(event.topic, event.message)
            is MqttEvents.SubscribeToTopic -> TODO()
        }
    }


    /**
     * connect to mqtt broker
     */
    private fun connect() {
        connectionMqttJob?.cancel()
        connectionMqttJob = viewModelScope.launch {
            mqttUseCase.connectToMQTT().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _mqttState.value = mqttState.value.copy(
                            isMqttConnected = true
                        )
                        getMessageFromPublisher()
                    }
                    is Resource.Failure -> updateErrorState(
                        "[Failed] Connect to MQTT caused by ${response.exception?.message}"
                    )
                    is Resource.Error -> updateErrorState(
                        "[Error] Connect to MQTT caused by ${response.exception.message}"
                    )
                    is Resource.UnknownError -> updateErrorState(
                        "[UnknownError] Connect to MQTT caused by ${response.exception.message}"
                    )
                    else -> throw IllegalStateException("Undefined State")
                }
            }
        }
    }

    fun getMessageFromPublisher() {
        viewModelScope.launch {
            mqttUseCase.retrieveMessageFromPublisher().collectLatest { response ->
                when (response) {
                    is Resource.MessageReceived -> onMessageReceived(response)
                    is Resource.ConnectionLost -> response.exception?.let { updateErrorState(it) }
                    is Resource.Error -> response.exception?.let { updateErrorState(it) }
                    is Resource.UnknownError -> response.exception?.let { updateErrorState(it) }
                    else -> throw IllegalStateException("Undefined state")
                }

            }
        }
    }

    fun subscribeToTopic(topic: String, qosLevel: Int?) {
        viewModelScope.launch {
            mqttUseCase.subscribeToTopic(topic, qosLevel).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _mqttState.value = mqttState.value.copy(
                            totemSubscriptionState = mqttState.value.totemSubscriptionState?.copy(
                                successfulTopics = mqttState.value.totemSubscriptionState?.successfulTopics.add(topic)
                            )
                        )
                    }
                    is Resource.Failure -> showToast(
                        "[Failed] subscribe to topic ${response.message} caused by ${response.exception?.message}"
                    )
                    is Resource.Error -> showToast(
                        "[Error] subscribe to topic ${response.message} caused by ${response.exception.message}"
                    )
                    is Resource.UnknownError -> showToast(
                        "[UnknownError] subscribe from topic ${response.message} caused by ${response.exception.message}"
                    )
                    else -> throw IllegalStateException("Undefined State")
                }
            }
        }
    }

    private fun onMessageReceived(messageReceived: Resource.MessageReceived<String>) {
        _mqttState.value = mqttState.value.copy(
            lastMessageReceived = Message(
                topic = messageReceived.topic,
                message = messageReceived.data
            )
        )
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
            error = e.message
        )
    }

    private fun updateErrorState(error: String) {
        _mqttState.value = mqttState.value.copy(
            error = error
        )
    }
}
