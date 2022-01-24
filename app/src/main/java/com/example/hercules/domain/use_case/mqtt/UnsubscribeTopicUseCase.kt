/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.mqtt

import com.example.hercules.domain.models.HerculesExceptions
import com.example.hercules.domain.repository.MqttRepository
import com.example.hercules.utils.Result
import javax.inject.Inject

class UnsubscribeTopicUseCase @Inject constructor(
    private val repo: MqttRepository
) {
    @Throws(HerculesExceptions::class)
    suspend operator fun invoke(topic: String): Result<String> {
        if (topic.isEmpty() || topic.isBlank()) throw HerculesExceptions.EmptyTopicException("No puedo desactivar un topic vacío")
        return repo.unSubscribe(topic)
    }
}