package com.example.watchit.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_639_1")
    val iso6391: String,
    @SerializedName("name")
    val name: String
)