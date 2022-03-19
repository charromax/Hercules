/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.sensors

import com.example.hercules.data.repository.TotemRepository
import com.example.hercules.domain.model.Totem
import javax.inject.Inject

class GetTotemByIdUseCase @Inject constructor(
    private val repo: TotemRepository
) {
    suspend operator fun invoke(id: Int): Totem? {
        return repo.getTotemByID(id)
    }
}