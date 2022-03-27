/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.local

import androidx.room.*
import com.example.hercules.data.model.DBTotem
import kotlinx.coroutines.flow.Flow

@Dao
interface TotemDao {

    @Query("SELECT * FROM totems")
    fun getAllTotems(): Flow<List<DBTotem>>

    @Query("SELECT * FROM totems WHERE id = :id")
    suspend fun getTotemByID(id: Int): DBTotem?

    @Query("SELECT * FROM totems WHERE topic = :topic")
    suspend fun getTotemByTopic(topic: String): DBTotem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewTotem(totem: DBTotem)

    @Update
    suspend fun updateTotem(totem: DBTotem)

    @Delete
    suspend fun deleteTotem(totem: DBTotem)

}