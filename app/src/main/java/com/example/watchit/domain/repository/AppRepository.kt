package com.example.watchit.domain.repository

import com.example.watchit.data.model.AccountResponse
import com.example.watchit.data.model.MovieDetail
import com.example.watchit.data.model.Movies
import com.example.watchit.data.model.MyWatchListMovies
import com.example.watchit.data.model.SessionResponse
import com.example.watchit.data.model.TrendMovie
import com.example.watchit.data.model.WatchListResponse
import com.example.watchit.data.model.WatchlistRequest
import retrofit2.Response


//specifies methods to use
interface AppRepository {
    suspend fun getMovies(page: Int) : Response<Movies>
    suspend fun getMovieDetail(movieId: Int): Response<MovieDetail>
    suspend fun getTrends(): Response<TrendMovie>
    suspend fun login(username: String, password: String): Result<SessionResponse>
    suspend fun addWatchlist(accountId: Int, sessionId: String,request: WatchlistRequest): Response<WatchListResponse>
    suspend fun getWatchlist(accountId: Int,sessionId: String): Response<MyWatchListMovies>
}