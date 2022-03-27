/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.remote.response

import com.squareup.moshi.JsonReader

data class MagSensorPayload(
    var data: Boolean = false
) : BaseTotemPayload() {
    companion object {
        fun buildPayload(json: JsonReader): MagSensorPayload? {
            var magSensorPayload: MagSensorPayload? = null
            json.beginObject()
            if (json.selectName(JsonReader.Options.of("data")) > -1)
                magSensorPayload = MagSensorPayload(json.nextBoolean())
            json.endObject()
            return magSensorPayload
        }
    }
}