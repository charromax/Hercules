/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.mqtt

import com.example.hercules.domain.models.HerculesExceptions
import com.example.hercules.domain.models.HerculesExceptions.InvalidMessageException
import com.example.hercules.domain.repository.MqttRepository
import com.example.hercules.utils.Result
import javax.inject.Inject

/**
 * publish orders to mqtt broker
 */
class PublishMqttUseCase @Inject constructor(
    private val repo: MqttRepository
) {
    @Throws(HerculesExceptions::class)
    suspend operator fun invoke(topic: String, data: String): Result<String> {
        if (topic.isEmpty()) throw InvalidMessageException("Topic vacio")
        if (data.isEmpty()) throw InvalidMessageException("Mensaje vacio")
        return repo.publish(topic, data)
    }
}