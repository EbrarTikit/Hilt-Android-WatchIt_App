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
            .build()
            .create(ApiInterface::class.java)

        return retrofit
    }


    @Provides
    @Singleton
    fun provideRepository(apiInterface: ApiInterface, app: Application): AppRepository {
        return AppRepositoryImpl(apiInterface,app)
    }
}