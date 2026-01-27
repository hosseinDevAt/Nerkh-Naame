package com.example.nerkhnaame.di.module

import com.example.nerkhnaame.data.remote.apiService.GoldAnalyzeApiService
import com.example.nerkhnaame.data.remote.apiService.GoldsApiService
import com.example.nerkhnaame.di.utils.GoldAnalyze
import com.example.nerkhnaame.di.utils.GoldsApi
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
    @GoldAnalyze
    @Provides
    fun provideGoldAnalyzeRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://hosme.ir/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Singleton
    @Provides
    fun provideGoldApiService(@GoldsApi retrofit: Retrofit): GoldsApiService =
        retrofit.create(GoldsApiService::class.java)


    @Singleton
    @Provides
    fun provideGoldAnalyzeApiService(@GoldAnalyze retrofit: Retrofit): GoldAnalyzeApiService =
        retrofit.create(GoldAnalyzeApiService::class.java)
}