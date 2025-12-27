package com.example.nerkhnaame.data.remote.apiService

import com.example.nerkhnaame.data.remote.model.HolidayModel
import retrofit2.http.GET
import retrofit2.http.Path

interface HolidayApiService {

    @GET("jalali/{year}/{month}/{day}")
    suspend fun getHolidayDetails(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Path("day") day: Int
    ): HolidayModel

}