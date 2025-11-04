package com.example.nerkhnaame.data.remote.apiService

import com.example.nerkhnaame.data.remote.model.GoldsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface GoldsApiService {

    @GET("Gold_Currency.php")
    suspend fun getGoldsData(
        @Query("key") apiKey: String
    ): GoldsModel

}