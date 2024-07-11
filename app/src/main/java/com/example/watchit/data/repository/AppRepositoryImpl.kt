package com.example.watchit.data.repository

import android.app.Application
import com.example.watchit.R
import com.example.watchit.data.model.Movies
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

}