package com.example.watchit.data.model

data class Movies(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)

data class Result(
    val description: String,
    val favorite_count: Int,
    val id: Int,
    val iso_639_1: String,
    val item_count: Int,
    val list_type: String,
    val name: String,
    val poster_path: String
)