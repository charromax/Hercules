/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hercules.domain.model.Sensor
import com.example.hercules.domain.use_case.sensors.SensorUseCases
import com.example.hercules.presentation.utils.Order
import com.example.hercules.presentation.utils.SensorOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorsViewModel @Inject constructor(
    private val sensorUseCases: SensorUseCases
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState

    private var recentlyDeletedSensor: Sensor? = null

    private var getAllSensorsJob: Job? = null

    init {
        getAllSensorsFromDB(SensorOrder.Topic(Order.Ascending))
    }

    /**
     * UI triggered events
     */
    fun onEvent(event: HomeEvents) {
        when (event) {
            is HomeEvents.OnDeleteSensor -> deleteSensor(event.sensor)
            is HomeEvents.OnOrderChange -> checkIfOrderChangedThenChangeOrder(event)
            HomeEvents.OnUndoDelete -> restoreDeletedSensor()
            HomeEvents.OnAddSensor -> navigateToAddSensorScreen()
            HomeEvents.OnToggleSectionOrder -> toggleOrderSectionVisibility()
        }
    }

    private fun navigateToAddSensorScreen() {
        viewModelScope.launch {
            _homeState.value = homeState.value.copy(
                navigateToAddSensorScreen = true
            )
        }
    }

    private fun toggleOrderSectionVisibility() {
        viewModelScope.launch {
            _homeState.value = homeState.value.copy(
                isOrderSectionVisible = !homeState.value.isOrderSectionVisible
            )
        }
    }

    /**
     * if sort order for sensor changed then reload all sensors
     */
    private fun checkIfOrderChangedThenChangeOrder(event: HomeEvents.OnOrderChange) {
        if (homeState.value.sensorOrder::class == event.sensorOrder::class
            && homeState.value.sensorOrder.order == event.sensorOrder.order
        ) return
        getAllSensorsFromDB(event.sensorOrder)
    }

    /**
     * restore a deleted sensor
     */
    private fun restoreDeletedSensor() {
        viewModelScope.launch {
            sensorUseCases.saveNewSensor(recentlyDeletedSensor?.toDBSensor() ?: return@launch)
            recentlyDeletedSensor = null
        }
    }

    /**
     * delete sensor from local DB
     */
    private fun deleteSensor(sensor: Sensor) {
        viewModelScope.launch {
            recentlyDeletedSensor = sensor
            sensorUseCases.deleteSensorUseCase(sensor)
        }
    }

    /**
     * cancelable sensors job, return list of sensors available
     */
    private fun getAllSensorsFromDB(sensorOrder: SensorOrder) {
        getAllSensorsJob?.cancel()
        getAllSensorsJob = sensorUseCases.getAllSensorsUseCase(sensorOrder)
            .onEach { sensors ->
                _homeState.value = homeState.value.copy(
                    sensors = sensors,
                    sensorOrder = sensorOrder
                )
            }.launchIn(viewModelScope)
    }
}