package com.example.watchit.data.network

import com.example.watchit.common.Const
import com.example.watchit.data.model.AccountDetails
import com.example.watchit.data.model.AccountResponse
import com.example.watchit.data.model.LoginRequest
import com.example.watchit.data.model.MovieDetail
import com.example.watchit.data.model.Movies
import com.example.watchit.data.model.MyWatchListMovies
import com.example.watchit.data.model.RequestTokenResponse
import com.example.watchit.data.model.SessionRequest
import com.example.watchit.data.model.SessionResponse
import com.example.watchit.data.model.TrendMovie
import com.example.watchit.data.model.WatchListResponse
import com.example.watchit.data.model.WatchlistRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    companion object {
        const val TOP_RATED = "3/movie/top_rated"
        const val DETAIL = "3/movie/{movieId}"
        const val TRENDS = "3/movie/popular"
        const val GET_TOKEN = "3/authentication/token/new"
        const val LOGIN ="3/authentication/token/validate_with_login"
        const val SESSION ="3/authentication/session/new"
        const val ACCOUNT_DETAILS = "3/account"
        const val WATCH_LIST = "3/account/{account_id}/watchlist"
        const val MY_WATCH_LIST = "3/account/{account_id}/watchlist/movies"
        const val UP_COMING = "3/movie/upcoming"
    }

    @GET(TOP_RATED)
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<Movies>

    @GET(DETAIL)
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<MovieDetail>

    @GET(TRENDS)
    suspend fun getTrends(
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<TrendMovie>

    @GET(UP_COMING)
    suspend fun getUpcoming(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<Movies>

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

    @GET(ACCOUNT_DETAILS)
    suspend fun getAccountDetails(
        @Query("api_key") apiKey: String = Const.API_KEY,
        @Query("session_id") sessionId: String
    ): Response<AccountResponse>

    @POST(WATCH_LIST)
    suspend fun addWatchList(
        @Path("account_id") accountId: Int,
        @Query("api_key") apiKey: String = Const.API_KEY,
        @Query("session_id") sessionId: String,
        @Body requestBody: WatchlistRequest
    ): Response<WatchListResponse>

    @GET(MY_WATCH_LIST)
    suspend fun getMyWatchList(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = Const.API_KEY
    ): Response<MyWatchListMovies>

    @GET("account/{account_id}")
    suspend fun getAccountDetails(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String
    ): Response<AccountDetails>

    @GET("account/{account_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String
    ): Response<Movies>

    @GET("account/{account_id}/watchlist/movies")
    suspend fun getWatchlist(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String
    ): Response<Movies>

}