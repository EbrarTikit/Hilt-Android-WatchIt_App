package com.example.watchit.data.network

import com.example.watchit.common.Const
import com.example.watchit.data.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/3/movie/top_rated")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<Movies>

    /**
     * const val TOP_RATED = "/3/movie/top_rated"
     *
     * @GET(NetworkConstants.EndPoints.TOP_RATED)
     * suspend fun getTopRatedMovies(
     *     @Query(NetworkConstants.Queries.PAGE) page: Int = 1,
     *     @Query(NetworkConstants.Queries.API_KEY) apiKey: String = NetworkConstants.API_KEY,
     *     @Query(NetworkConstants.Queries.LANGUAGE) language: String = "tr-TR"
     * ): NetworkMovie
     */

}