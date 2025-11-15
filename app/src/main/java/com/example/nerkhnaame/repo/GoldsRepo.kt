package com.example.nerkhnaame.repo

import com.example.nerkhnaame.data.remote.apiService.GoldsApiService
import com.example.nerkhnaame.data.remote.utils.ApiRepoHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoldsRepo @Inject constructor(
    private val apiService: GoldsApiService,
    private val apiRepo: ApiRepoHandler
) {

    suspend fun getGoldsPrice(apiKey: String) = apiRepo.getSafeDataRepo { apiService.getGoldsData(apiKey) }

}