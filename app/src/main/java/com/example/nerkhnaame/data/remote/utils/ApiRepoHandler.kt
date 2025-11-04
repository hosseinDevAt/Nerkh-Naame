package com.example.nerkhnaame.data.remote.utils

import android.util.Log
import com.google.gson.JsonSyntaxException
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepoHandler @Inject constructor() {

    suspend fun <T> getSafeDataRepo(request: suspend () -> T): Result<T> {

        return try {
            val response = request()
            Result.success(response)
        }catch (e: Exception){
            when(e){
                is SocketTimeoutException -> Log.e("TAG", "TimeOut")
                is IOException -> Log.e("TAG", "Connection Error")
                is HttpException -> Log.e("TAG", "Http ${e.code()} - ${e.message()}")
                is JsonSyntaxException -> Log.e("TAG", "Json Error")
                else -> Log.e("TAG", e.message.toString())
            }
            Result.failure(e)
        }

    }

}