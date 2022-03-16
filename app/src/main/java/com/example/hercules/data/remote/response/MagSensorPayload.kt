/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.remote.response

import com.squareup.moshi.JsonReader

data class MagSensorPayload(
    val data: Boolean
) : BaseTotemPayload() {
    companion object {
        fun buildPayload(json: JsonReader): BaseTotemPayload? {
            return when (json.selectName(JsonReader.Options.of("data"))) {
                0 -> MagSensorPayload(json.nextBoolean())
                else -> null
            }
        }
    }
}