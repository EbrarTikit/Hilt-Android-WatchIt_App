package com.example.watchit.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tmdb(
    @SerialName("avatar_path")
    val avatarPath: String? = null
)