/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.mqtt

import com.example.hercules.domain.model.Message
import com.example.hercules.data.repository.MqttRepository
import com.example.hercules.presentation.utils.Result
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val repo: MqttRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(): Result<Message> {
        return repo.receiveMessages()
    }
}