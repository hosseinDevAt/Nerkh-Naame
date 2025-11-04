package com.example.nerkhnaame.di.module

import com.example.nerkhnaame.data.remote.apiService.GoldsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiHandler {

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()


    @Singleton
    @Provides
    fun provideGoldsRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://BrsApi.ir/Api/Market/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): GoldsApiService =
        retrofit.create(GoldsApiService::class.java)

}