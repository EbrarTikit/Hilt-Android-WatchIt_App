package com.example.watchit.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SessionRequest(
    @SerialName("request_token")
    val requestToken: String
)
