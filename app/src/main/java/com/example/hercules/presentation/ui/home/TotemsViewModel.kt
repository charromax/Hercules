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

    fun updateTotemName(value: String) {
        _addTotemScreenState.value = addTotemScreenState.value.copy(
            newTotemName = value
        )
    }

    fun updateTotemTopic(value: String) {
        _addTotemScreenState.value = addTotemScreenState.value.copy(
            newTotemTopic = value
        )
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
        addTotem(
            DBTotem(
                topic = _addTotemScreenState.value.newTotemTopic ?: "",
                name = _addTotemScreenState.value.newTotemName ?: "",
                totemType = _addTotemScreenState.value.selectedTotemType
            )
        )
        _addTotemScreenState.value = addTotemScreenState.value.copy(
            snack = "Totem guardado!",
            isSaveSuccessful = true
        )
    }

    private fun checkTotemTopic(): Boolean {
        if (_addTotemScreenState.value.newTotemTopic?.isBlank() == true) return true
        if (_addTotemScreenState.value.newTotemTopic?.contains(" ") == true) return true
        if (_addTotemScreenState.value.newTotemTopic?.contains("/") == false) return true
        if (_addTotemScreenState.value.newTotemTopic?.contains("\\") == true) return true
        return false
    }

    private fun checkTotemName(): Boolean {
        return _addTotemScreenState.value.newTotemName?.isBlank() == true
    }

    private fun updateSelectedTotem(type: TotemType) {
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