package com.example.watchit.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendMovie(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<TrendResult>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)