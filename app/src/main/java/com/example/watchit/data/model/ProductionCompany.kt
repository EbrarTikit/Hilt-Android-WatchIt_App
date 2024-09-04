package com.example.watchit.data.model


import com.google.gson.annotations.SerializedName

import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompany(
    @SerializedName("id")
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)