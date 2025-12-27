package com.example.nerkhnaame.di.module

import com.example.nerkhnaame.data.remote.apiService.GoldsApiService
import com.example.nerkhnaame.data.remote.apiService.HolidayApiService
import com.example.nerkhnaame.di.utils.GoldsApi
import com.example.nerkhnaame.di.utils.HolidayApi
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
    @GoldsApi
    @Provides
    fun provideGoldsRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://BrsApi.ir/Api/Market/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Singleton
    @HolidayApi
    @Provides
    fun provideHolidayRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://holidayapi.ir/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Singleton
    @Provides
    fun provideGoldApiService(@GoldsApi retrofit: Retrofit): GoldsApiService =
        retrofit.create(GoldsApiService::class.java)


    @Singleton
    @Provides
    fun provideHolidayApiService(@HolidayApi retrofit: Retrofit): HolidayApiService =
        retrofit.create(HolidayApiService::class.java)
}