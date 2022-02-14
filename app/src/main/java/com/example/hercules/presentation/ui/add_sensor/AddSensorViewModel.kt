/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.add_sensor

import androidx.lifecycle.ViewModel
import com.example.hercules.domain.use_case.sensors.SensorUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddSensorViewModel @Inject constructor(
    private val sensorUseCases: SensorUseCases
) : ViewModel() {

    /**
     * create and save and new sensor in
     * database, using topic
     */
    fun onSaveNewSensor(topic: String) {

    }
}