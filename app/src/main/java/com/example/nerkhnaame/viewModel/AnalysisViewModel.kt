package com.example.nerkhnaame.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nerkhnaame.data.remote.model.GoldAnalysisItem
import com.example.nerkhnaame.repo.GoldAnalyzeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val analyzeRepo: GoldAnalyzeRepo
): ViewModel() {

    private val _state = MutableStateFlow(AnalysisState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        fetchAnalysis()
    }

    fun fetchAnalysis(){

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            analyzeRepo.getGoldsAnalyze()
                .onSuccess {
                    val data = it.data
                    _state.update {
                        it.copy(
                            isLoading = false,
                            gold18k = data.gold_18k,
                            gold24k = data.gold_24k,
                            coin = data.coin,
                            error = null
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "خطای نامشخص رخ داده است."
                        )
                    }
                }

        }

    }




}


data class AnalysisState(
    val isLoading: Boolean = false,
    val gold18k: GoldAnalysisItem? = null,
    val gold24k: GoldAnalysisItem? = null,
    val coin: GoldAnalysisItem? = null,
    val error: String? = null
){
    fun allItems(): List<GoldAnalysisItem> =
        listOfNotNull(gold18k, gold24k, coin)
}

