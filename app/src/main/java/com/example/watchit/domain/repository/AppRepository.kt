package com.example.watchit.domain.repository

import com.example.watchit.data.model.MovieDetail
import com.example.watchit.data.model.Movies
import com.example.watchit.data.model.TrendMovie
import retrofit2.Response


//specifies methods to use
interface AppRepository {
    suspend fun getMovies(page: Int) : Response<Movies>
    suspend fun getMovieDetail(movieId: Int): Response<MovieDetail>
    suspend fun getTrends(): Response<TrendMovie>
}