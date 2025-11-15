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
): ViewModel() {

    private val _golds = MutableStateFlow(emptyList<Gold>())
    val golds = _golds.asStateFlow()


    init {
        getGolds()
    }


    fun getGolds(){
        viewModelScope.launch(Dispatchers.IO) {

            repo.getGoldsPrice("BfTYbSljKInixBiTv46G6fffvp9DdhGe")
                .onSuccess { gold ->
                    _golds.value = gold.gold
                }
        }
    }

}