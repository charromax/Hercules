/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.mqtt

import android.content.Context
import com.example.hercules.R
import com.example.hercules.domain.models.HerculesExceptions
import com.example.hercules.domain.models.HerculesExceptions.EmptyTopicException
import com.example.hercules.domain.repository.MqttRepository
import com.example.hercules.utils.Result
import javax.inject.Inject

class ConnectMqttUseCase @Inject constructor(
    private val repo: MqttRepository
) {
    @Throws(HerculesExceptions::class)
    suspend operator fun invoke(context: Context?, topics: List<String>): Result<String> {
        if (context == null) throw Exception("Null context")
        if (topics.isEmpty()) throw EmptyTopicException(context.getString(R.string.empty_topics_list))
        return repo.connect(context, topics)
    }
}