package com.example.watchit.di

import android.app.Application
import com.example.watchit.common.Const
import com.example.watchit.data.network.ApiInterface
import com.example.watchit.data.repository.AppRepositoryImpl
import com.example.watchit.domain.repository.AppRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): ApiInterface {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }

        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(httpLoggingInterceptor())
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(apiInterface: ApiInterface, app: Application): AppRepository {
        return AppRepositoryImpl(apiInterface, app)
    }

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}