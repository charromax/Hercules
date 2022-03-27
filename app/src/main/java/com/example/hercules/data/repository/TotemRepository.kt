/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.repository

import com.example.hercules.data.model.DBTotem
import com.example.hercules.domain.model.Totem
import kotlinx.coroutines.flow.Flow

interface TotemRepository {
    fun getAllTotems(): Flow<List<Totem>>
    suspend fun getTotemByID(id: Int): Totem?
    suspend fun getTotemByTopic(topic: String): DBTotem?
    suspend fun addNewTotem(totem: DBTotem)
    suspend fun updateTotem(totem: DBTotem)
    suspend fun deleteSensor(totem: DBTotem)
}