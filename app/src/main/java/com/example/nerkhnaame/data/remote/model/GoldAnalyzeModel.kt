package com.example.nerkhnaame.data.remote.model


data class Advice(
    val action: String,
    val title: String,
    val description: String
)

data class MarketStats(
    val current_price: String,
    val change_percent: String
)


data class GoldAnalysisItem(
    val title: String,
    val advice: Advice,
    val market_stats: MarketStats?,
    val chart_points: List<Long>,
    val chart_labels: List<String>
)


data class GoldAnalysisData(
    val gold_18k: GoldAnalysisItem,
    val gold_24k: GoldAnalysisItem,
    val coin: GoldAnalysisItem
)


data class AnalysisResponse(
    val status: String,
    val data: GoldAnalysisData
)
