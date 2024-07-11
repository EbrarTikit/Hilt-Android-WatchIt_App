package com.example.watchit.data.network

import com.example.watchit.data.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/list/movies")
    suspend fun getMovies(
        @Query("page") page: Int
    ): Response<Movies>

}