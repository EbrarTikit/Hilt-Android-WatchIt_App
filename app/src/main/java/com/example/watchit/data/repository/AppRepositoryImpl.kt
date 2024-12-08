package com.example.watchit.data.repository

import android.app.Application
import com.example.watchit.R
import com.example.watchit.data.model.LoginRequest
import com.example.watchit.data.model.MovieDetail
import com.example.watchit.data.model.Movies
import com.example.watchit.data.model.SessionRequest
import com.example.watchit.data.model.SessionResponse
import com.example.watchit.data.model.TrendMovie
import com.example.watchit.data.network.ApiInterface
import com.example.watchit.domain.repository.AppRepository
import retrofit2.Response
import javax.inject.Inject

//actual implementation of methods done here
class AppRepositoryImpl @Inject constructor(
    private val api: ApiInterface,
    private val appContext: Application
): AppRepository{

    init {
        val appName = appContext.getString(R.string.app_name)
        println("Hello The app name is $appName")
    }
    override suspend fun getMovies(page: Int): Response<Movies> {
        return api.getMovies(page)
    }

    override suspend fun getMovieDetail(movieId: Int): Response<MovieDetail> {
        return api.getMovieDetail(movieId)
    }

    override suspend fun getTrends(): Response<TrendMovie>{
        return api.getTrends()
    }

    override suspend fun login(username: String, password: String): Result<SessionResponse> {
        return try {
            // 1. Request token al
            val tokenResponse = api.createRequestToken()
            if (!tokenResponse.isSuccessful) {
                return Result.failure(Exception("Failed to create request token"))
            }

            val requestToken = tokenResponse.body()?.requestToken ?: return Result.failure(Exception("Invalid token response"))

            // 2. Login ile token'ı doğrula
            val loginResponse = api.validateWithLogin(
                request = LoginRequest(username, password, requestToken)
            )
            if (!loginResponse.isSuccessful) {
                return Result.failure(Exception("Login failed"))
            }

            // 3. Session oluştur
            val sessionResponse = api.createSession(
                request = SessionRequest(requestToken)
            )
            if (!sessionResponse.isSuccessful) {
                return Result.failure(Exception("Failed to create session"))
            }

            Result.success(sessionResponse.body()!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}