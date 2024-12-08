package com.example.watchit.data.network

import com.example.watchit.common.Const
import com.example.watchit.data.model.LoginRequest
import com.example.watchit.data.model.MovieDetail
import com.example.watchit.data.model.Movies
import com.example.watchit.data.model.RequestTokenResponse
import com.example.watchit.data.model.SessionRequest
import com.example.watchit.data.model.SessionResponse
import com.example.watchit.data.model.TrendMovie
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET(TOP_RATED)
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<Movies>

    companion object {
        const val TOP_RATED = "3/movie/top_rated"
        const val DETAIL = "3/movie/{movieId}"
        const val TRENDS = "3/movie/popular"
        const val GET_TOKEN = "3/authentication/token/new"
        const val LOGIN ="3/authentication/token/validate_with_login"
        const val SESSION ="3/authentication/session/new"
    }

    @GET(DETAIL)
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<MovieDetail>

    @GET(TRENDS)
    suspend fun getTrends(
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<TrendMovie>

    @GET(GET_TOKEN)
    suspend fun createRequestToken(
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<RequestTokenResponse>

    @POST(LOGIN)
    suspend fun validateWithLogin(
        @Query("api_key") apiKey: String = Const.API_KEY,
        @Body request: LoginRequest
    ): Response<RequestTokenResponse>

    @POST(SESSION)
    suspend fun createSession(
        @Query("api_key") apiKey: String = Const.API_KEY,
        @Body request: SessionRequest
    ): Response<SessionResponse>

}