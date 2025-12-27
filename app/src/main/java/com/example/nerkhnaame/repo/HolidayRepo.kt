package com.example.nerkhnaame.repo

import com.example.nerkhnaame.data.remote.apiService.HolidayApiService
import com.example.nerkhnaame.data.remote.utils.ApiRepoHandler
import javax.inject.Inject

class HolidayRepo @Inject constructor(
    private val apiService: HolidayApiService,
    private val apiRepo: ApiRepoHandler
) {

    suspend fun getHolidays(year: Int, month: Int, day: Int) =
        apiRepo.getSafeDataRepo { apiService.getHolidayDetails(year, month, day) }

}