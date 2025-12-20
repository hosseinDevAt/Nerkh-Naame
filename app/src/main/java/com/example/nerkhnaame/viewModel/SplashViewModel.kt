package com.example.nerkhnaame.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nerkhnaame.utils.ConnectivityObserver
import com.example.nerkhnaame.utils.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _networkState = MutableStateFlow<NetworkUiState>(NetworkUiState.Loading)
    val networkState = _networkState.asStateFlow()

    fun checkNetwork() {
        _networkState.value = NetworkUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            delay(3500L)
            connectivityObserver.observe().collect { status ->
                val isConnected = status is NetworkStatus.Available
                _networkState.value = NetworkUiState.Success(isConnected = isConnected)
                return@collect
            }
        }
    }

}

sealed interface NetworkUiState {
    object Loading : NetworkUiState
    data class Success(val isConnected: Boolean) : NetworkUiState
    data class Error(val message: String) : NetworkUiState
}
