package com.example.watchit.di

import android.app.Application
import com.example.watchit.common.Const
import com.example.watchit.data.network.ApiInterface
import com.example.watchit.data.repository.AppRepositoryImpl
import com.example.watchit.domain.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit() : ApiInterface {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpLoggingInterceptor())
            .build()
            .create(ApiInterface::class.java)

        return retrofit
    }


    @Provides
    @Singleton
    fun provideRepository(apiInterface: ApiInterface, app: Application): AppRepository {
        return AppRepositoryImpl(apiInterface,app)
    }

    //logcat kontrolü
    @Provides
    @Singleton
    fun  httpLoggingInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}