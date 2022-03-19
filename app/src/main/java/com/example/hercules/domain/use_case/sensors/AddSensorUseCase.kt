/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.sensors

import com.example.hercules.data.model.DBTotem
import com.example.hercules.data.repository.TotemRepository
import com.example.hercules.domain.model.InvalidSensorException
import javax.inject.Inject

class AddSensorUseCase @Inject constructor(
    private val repository: TotemRepository
) {
    @Throws(InvalidSensorException::class)
    suspend operator fun invoke(totem: DBTotem) {
        if (totem.topic.isEmpty()) throw InvalidSensorException("El topic no puede estar vacío")
        if (totem.topic.isBlank()) throw InvalidSensorException("El topic es inválido")
        repository.addNewTotem(totem)
    }
}