/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hercules.data.model.DBTotem
import com.example.hercules.domain.model.Totem
import com.example.hercules.domain.model.TotemType
import com.example.hercules.domain.use_case.sensors.TotemUseCases
import com.example.hercules.presentation.ui.add_sensor.AddTotemScreenEvents
import com.example.hercules.presentation.ui.add_sensor.AddTotemScreenState
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
class TotemsViewModel @Inject constructor(
    private val totemUseCases: TotemUseCases
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState

    private val _addTotemScreenState = MutableStateFlow(AddTotemScreenState())
    val addTotemScreenState: StateFlow<AddTotemScreenState> = _addTotemScreenState

    private var recentlyDeletedSensor: Totem? = null

    var newTotemName = ""
    var newTotemTopic = ""
    var newTotemType: TotemType? = null

    private var getAllSensorsJob: Job? = null

    init {
        getAllSensorsFromDB(SensorOrder.Topic(Order.Ascending))
    }

    /**
     * UI triggered events
     */
    fun onEvent(event: HomeEvents) {
        when (event) {
            is HomeEvents.OnDeleteTotem -> deleteTotem(event.totem)
            is HomeEvents.OnOrderChange -> checkIfOrderChangedThenChangeOrder(event)
            HomeEvents.OnUndoDelete -> restoreDeletedSensor()
            is HomeEvents.OnAddTotem -> addTotem(event.totem)
            HomeEvents.OnToggleSectionOrder -> toggleOrderSectionVisibility()
        }
    }

    /**
     * UI triggered events
     */
    fun onEvent(event: AddTotemScreenEvents) {
        when (event) {
            is AddTotemScreenEvents.OnAddNewTotem -> onAddNewTotemClicked()
            AddTotemScreenEvents.OnDropDownDismissed -> onDropDownUpdate()
            AddTotemScreenEvents.OnDropDownOpen -> onDropDownUpdate()
            is AddTotemScreenEvents.OnTotemTypeSelected -> updateSelectedTotem(event.type)
        }
    }

    private fun onAddNewTotemClicked() {
        if (checkTotemName()) {
            updateErrorState("Nombre inválido")
            return
        }
        if (checkTotemTopic()) {
            updateErrorState("Topic inválido")
            return
        }
        newTotemType?.let {
            addTotem(
                DBTotem(
                    topic = newTotemTopic,
                    name = newTotemName,
                    totemType = it
                )
            )
            _addTotemScreenState.value = addTotemScreenState.value.copy(
                snack = "Totem guardado!"
            )
        } ?: run {
            updateErrorState("Totem no reconocido")
            return
        }

    }

    private fun checkTotemTopic(): Boolean {
        if (newTotemTopic.isBlank()) return true
        if (newTotemTopic.contains(" ")) return true
        if (!newTotemTopic.contains("/")) return true
        if (newTotemTopic.contains("\\")) return true
        return false
    }

    private fun checkTotemName(): Boolean {
        return newTotemName.isBlank()
    }

    private fun updateSelectedTotem(type: TotemType) {
        newTotemType = type
        _addTotemScreenState.value = addTotemScreenState.value.copy(
            selectedTotemType = type
        )
    }

    private fun onDropDownUpdate() {
        _addTotemScreenState.value = addTotemScreenState.value.copy(
            isDropDownExpanded = !addTotemScreenState.value.isDropDownExpanded
        )
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
            totemUseCases.saveNewSensor(recentlyDeletedSensor?.toDBObject() ?: return@launch)
            recentlyDeletedSensor = null
        }
    }

    /**
     * delete sensor from local DB
     */
    private fun deleteTotem(totem: Totem) {
        viewModelScope.launch {
            recentlyDeletedSensor = totem
            totemUseCases.deleteSensorUseCase(totem)
        }
    }

    /**
     * cancelable sensors job, return list of sensors available
     */
    private fun getAllSensorsFromDB(sensorOrder: SensorOrder) {
        getAllSensorsJob?.cancel()
        getAllSensorsJob = totemUseCases.getAllSensorsUseCase(sensorOrder)
            .onEach { totems ->
                _homeState.value = homeState.value.copy(
                    totems = totems,
                    topicList = totems.map { it.topic },
                    sensorOrder = sensorOrder
                )
            }.launchIn(viewModelScope)
    }

    private fun addTotem(totem: DBTotem) {
        viewModelScope.launch {
            totemUseCases.saveNewSensor(totem)
        }
    }

    private fun updateErrorState(error: String) {
        _addTotemScreenState.value = addTotemScreenState.value.copy(
            error = error
        )
    }
}