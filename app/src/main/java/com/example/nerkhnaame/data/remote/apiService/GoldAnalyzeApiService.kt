package com.example.nerkhnaame.data.remote.apiService

import com.example.nerkhnaame.data.remote.model.AnalysisResponse
import retrofit2.http.GET

interface GoldAnalyzeApiService {
    @GET("/api/gold/analyze")
    suspend fun getGoldAnalyze(): AnalysisResponse
}