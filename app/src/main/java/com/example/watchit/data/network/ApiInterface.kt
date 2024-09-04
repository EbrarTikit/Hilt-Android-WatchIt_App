package com.example.watchit.data.network

import com.example.watchit.common.Const
import com.example.watchit.data.model.MovieDetail
import com.example.watchit.data.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET(TOP_RATED)
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<Movies>

    companion object {
        const val TOP_RATED = "/3/movie/top_rated"
        const val DETAIL = "/3/movie/{movieId}"
    }

    @GET(DETAIL)
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<MovieDetail>

}