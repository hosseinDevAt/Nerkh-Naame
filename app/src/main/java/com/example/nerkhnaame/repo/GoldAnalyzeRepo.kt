package com.example.nerkhnaame.repo

import com.example.nerkhnaame.data.remote.apiService.GoldAnalyzeApiService
import com.example.nerkhnaame.data.remote.utils.ApiRepoHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoldAnalyzeRepo @Inject constructor(
    private val apiService: GoldAnalyzeApiService,
    private val apiRepo: ApiRepoHandler
) {

    suspend fun getGoldsAnalyze() = apiRepo.getSafeDataRepo { apiService.getGoldAnalyze() }

}