package com.example.nerkhnaame.di.module

import com.example.nerkhnaame.utils.ConnectivityObserver
import com.example.nerkhnaame.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkHandler {

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        observer: NetworkMonitor
    ): ConnectivityObserver

}