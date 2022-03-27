/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.model

import androidx.room.TypeConverter
import com.example.hercules.data.remote.response.MagSensorPayload
import com.squareup.moshi.JsonReader
import okio.Buffer

class MagSensorPayloadTypeConverter {
    @TypeConverter
    fun toMagSensorPayload(data: String): MagSensorPayload {
        val jsonReader = JsonReader.of(Buffer().writeUtf8(data))
        jsonReader.beginObject()
        return MagSensorPayload.buildPayload(json = jsonReader) ?: MagSensorPayload()
    }
}