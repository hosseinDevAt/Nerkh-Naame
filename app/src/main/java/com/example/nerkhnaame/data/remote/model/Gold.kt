package com.example.nerkhnaame.data.remote.model

data class Gold(
    val change_percent: Double,
    val change_value: Int,
    val date: String,
    val name: String,
    val name_en: String,
    val price: Int,
    val symbol: String,
    val time: String,
    val time_unix: Int,
    val unit: String
)