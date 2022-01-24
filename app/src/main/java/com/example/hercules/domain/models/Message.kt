/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.models

import org.joda.time.DateTime

data class Message(
    val createdAt: String = DateTime.now().toDateTimeISO().toString(),
    val topic: String,
    val message: String
)

