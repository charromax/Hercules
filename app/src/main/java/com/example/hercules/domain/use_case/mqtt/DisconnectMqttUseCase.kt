/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.mqtt

import com.example.hercules.data.repository.MqttRepository
import com.example.hercules.presentation.utils.Result
import javax.inject.Inject

/**
 * disconnect mqtt client from broker and reset retries
 */
class DisconnectMqttUseCase @Inject constructor(
    private val repo: MqttRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(): Result<String> {
        return repo.disconnect()
    }
}