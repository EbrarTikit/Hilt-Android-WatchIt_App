package com.example.watchit.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchlistRequest(
    @SerialName("media_type")
    val mediaType: String,
    @SerialName("media_id")
    val mediaId: Int,
    @SerialName("watchlist")
    val watchlist: Boolean
)

