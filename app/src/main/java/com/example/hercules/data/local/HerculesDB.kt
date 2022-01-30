/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hercules.data.model.DBSensor

// Annotates class to be a Room Database with a table (entity) of the Sensor class
@Database(entities = [DBSensor::class], version = 1, exportSchema = false)
abstract class HerculesDB : RoomDatabase() {

    abstract fun sensorDao(): SensorDao

}