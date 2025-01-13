package com.example.watchit.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDetails(
    val id: Int,
    val name: String,
    val username: String,
    @SerialName("include_adult")
    val includeAdult: Boolean,
    @SerialName("avatar")
    val avatar: Avatar
) 