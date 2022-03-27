/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.remote.response

import com.squareup.moshi.JsonReader

enum class WaterCycles {
    MANUAL, CYCLE, SCHEDULE
}

data class WaterPumpPayload(
    var isWorking: Boolean = false,
    var currentCycle: WaterCycles = WaterCycles.MANUAL
) : BaseTotemPayload() {
    companion object {
        val IS_WORKING = "is_working"
        val CYCLE = "cycle"
        fun buildPayload(
            json: JsonReader
        ): BaseTotemPayload {
            json.beginObject()
            val payload = WaterPumpPayload()
            if (json.selectName(JsonReader.Options.of(IS_WORKING)) > -1) payload.isWorking =
                json.nextBoolean()
            if (json.selectName(JsonReader.Options.of(CYCLE)) > -1) payload.currentCycle =
                WaterCycles.valueOf(json.nextString().uppercase())
            json.endObject()
            return payload
        }
    }
}