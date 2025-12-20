package com.example.nerkhnaame.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nerkhnaame.data.remote.model.Gold
import com.example.nerkhnaame.repo.GoldsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: GoldsRepo
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state = _state.asStateFlow()


    init {
        getGolds()
    }


    fun getGolds() {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getGoldsPrice("BfTYbSljKInixBiTv46G6fffvp9DdhGe")
                .onSuccess { gold ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        golds = gold.gold,
                        error = null
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "خطای نامشخصی رخ داده است"
                    )
                }
        }
    }

}

data class HomeState(
    val isLoading: Boolean = false,
    val golds: List<Gold> = emptyList(),
    val error: String? = null
)