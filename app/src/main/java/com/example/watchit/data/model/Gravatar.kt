package com.example.watchit.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gravatar(
    @SerialName("hash")
    val hash: String
)