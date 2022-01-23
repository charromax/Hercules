package com.example.hercules.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hercules.domain.use_case.mqtt.MqttUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MqttViewModel @Inject constructor(
    private val mqttUseCases: MqttUseCases
): ViewModel() {
    private val _mqttState = MutableStateFlow<MqttState>(MqttState(null, null))
        val mqttState: StateFlow<MqttState> = _mqttState

    fun onEvent(event:MqttEvents) {
        when (event) {
            is MqttEvents.StartConnectionRequest -> connect(event.context, event.topics)
        }
    }

    private fun connect(context: Context, topics: List<String>) {
        viewModelScope.launch {
            try {
                mqttUseCases.connectMqttUseCase(context, topics)
            } catch (e: Exception) {
                _mqttState.value = mqttState.value.copy(
                    message = null,
                    error = e
                )
            }
        }
    }
}
