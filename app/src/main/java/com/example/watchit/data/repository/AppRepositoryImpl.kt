package com.example.watchit.data.repository

import android.app.Application
import com.example.watchit.R
import com.example.watchit.data.model.AccountResponse
import com.example.watchit.data.model.LoginRequest
import com.example.watchit.data.model.MovieDetail
import com.example.watchit.data.model.Movies
import com.example.watchit.data.model.MyWatchListMovies
import com.example.watchit.data.model.SessionRequest
import com.example.watchit.data.model.SessionResponse
import com.example.watchit.data.model.TrendMovie
import com.example.watchit.data.model.WatchListResponse
import com.example.watchit.data.model.WatchlistRequest
import com.example.watchit.data.network.ApiInterface
import com.example.watchit.domain.repository.AppRepository
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

//actual implementation of methods done here
class AppRepositoryImpl @Inject constructor(
    private val api: ApiInterface,
    private val appContext: Application
): AppRepository{

    private val sharedPreferences = appContext.getSharedPreferences("AppPrefs", Application.MODE_PRIVATE)

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
                val errorBody = tokenResponse.errorBody()?.string()
                return Result.failure(Exception("Failed to create request token: $errorBody"))
            }

            val requestToken = tokenResponse.body()?.requestToken 
                ?: return Result.failure(Exception("Invalid token response"))

            // 2. Login ile token'ı doğrula
            val loginResponse = api.validateWithLogin(
                request = LoginRequest(
                    username = username,
                    password = password,
                    requestToken = requestToken
                )
            )
            if (!loginResponse.isSuccessful) {
                val errorBody = loginResponse.errorBody()?.string()
                return Result.failure(Exception("Login failed: $errorBody"))
            }

            // 3. Session oluştur
            val sessionResponse = api.createSession(
                request = SessionRequest(requestToken = requestToken)
            )
            if (!sessionResponse.isSuccessful) {
                val errorBody = sessionResponse.errorBody()?.string()
                return Result.failure(Exception("Failed to create session: $errorBody"))
            }

            // Session ID'yi kaydet
            sessionResponse.body()?.sessionId?.let { sessionId ->
                sharedPreferences.edit()
                    .putString("SESSION_ID", sessionId)
                    .apply()
            }

            // Account details'i al
            val accountResponse = api.getAccountDetails(sessionId = sessionResponse.body()?.sessionId!!)
            if (accountResponse.isSuccessful) {
                val accountId = accountResponse.body()?.id
                sharedPreferences.edit()
                    .putInt("ACCOUNT_ID", accountId ?: -1)
                    .apply()
            } else {
                val errorBody = accountResponse.errorBody()?.string()
                return Result.failure(Exception("Failed to get account details: $errorBody"))
            }

            Result.success(sessionResponse.body()!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getAccountId(): Int? {
        val id = sharedPreferences.getInt("ACCOUNT_ID", -1)
        return if (id == -1) null else id
    }

    override suspend fun addWatchlist(
        accountId: Int,
        sessionId: String,
        request: WatchlistRequest
    ): Response<WatchListResponse> {
        val accId = getAccountId() ?: accountId
        return try {
            api.addWatchList(
                accountId = accId,
                sessionId = sessionId,
                requestBody = request
            ).also { response ->
                if (!response.isSuccessful) {
                    throw HttpException(response)
                }
            }
        } catch (e: HttpException) {
            throw Exception("API Error: ${e.message}")
        } catch (e: IOException) {
            throw Exception("Network Error: ${e.message}")
        } catch (e: Exception) {
            throw Exception("Unknown Error: ${e.message}")
        }
    }

    override suspend fun getWatchlist(
        accountId: Int,
        sessionId: String
    ): Response<MyWatchListMovies> {
        val accId = getAccountId() ?: accountId
        return api.getMyWatchList(accId,sessionId)
    }


}