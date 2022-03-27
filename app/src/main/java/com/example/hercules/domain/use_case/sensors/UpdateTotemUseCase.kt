/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.sensors

import com.example.hercules.data.model.DBTotem
import com.example.hercules.data.repository.TotemRepository
import javax.inject.Inject

class UpdateTotemUseCase @Inject constructor(
    private val repo: TotemRepository
) {
    suspend operator fun invoke(totem: DBTotem) {
        return repo.updateTotem(totem)
    }
}