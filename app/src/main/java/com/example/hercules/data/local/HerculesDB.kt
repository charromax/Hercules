/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hercules.data.model.DBTotem
import com.example.hercules.data.model.MagSensorPayloadTypeConverter

// Annotates class to be a Room Database with a table (entity) of the Sensor class
@Database(
    entities = [DBTotem::class],
    version = 3,
    exportSchema = false,
)
@TypeConverters(MagSensorPayloadTypeConverter::class)
abstract class HerculesDB : RoomDatabase() {

    abstract fun sensorDao(): TotemDao

}