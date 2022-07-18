package com.example.hercules.data.remote.requests

import com.example.hercules.domain.model.RangeChannel
import com.example.hercules.domain.model.RgbMode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LedStripControlRequest(
    val mode: RgbMode,
    val speed: Int,
    @field:Json(name = "range_channel")
    val rangeChannel: RangeChannel,
    val red: Int,
    val green: Int,
    val blue: Int,
)