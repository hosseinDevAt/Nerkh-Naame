package com.example.nerkhnaame.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nerkhnaame.data.remote.model.Gold
import com.example.nerkhnaame.repo.GoldsRepo
import com.example.nerkhnaame.repo.HolidayRepo
import com.example.nerkhnaame.utils.PersianDate
import com.example.nerkhnaame.utils.fa
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val goldsRepo: GoldsRepo,
    private val holidayRepo: HolidayRepo
) : ViewModel() {

    private val pDate = PersianDate()
    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state = _state.asStateFlow()


    init {
        getGolds()
        getTodayDate()
        getHolidaysByDate()
    }

    fun getHolidaysByDate(){
        viewModelScope.launch(Dispatchers.IO) {

            holidayRepo.getHolidays(
                pDate.year,
                pDate.month,
                pDate.day
            ).onSuccess {
                _state.value = _state.value.copy(
                    holiday = if(it.events.isNotEmpty()){
                        it.events[0].description
                    } else {
                        "هیچ مناسبتی برای امروز وجود ندارد"
                    }
                )
            }

        }
    }

    private fun getTodayDate() {
        val formatedDate = "${pDate.strWeekDay} ${pDate.day} ${pDate.strMonth} ${pDate.year}".fa()
        _state.value = _state.value.copy(todayDate = formatedDate)
    }


    fun getGolds() {
        viewModelScope.launch(Dispatchers.IO) {

            _state.update {
                it.copy(isLoading = true)
            }

            delay(500L)

            goldsRepo.getGoldsPrice("BfTYbSljKInixBiTv46G6fffvp9DdhGe")
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
    val error: String? = null,
    val todayDate: String = "در حال دریافت تاریخ...",
    val holiday: String = "در حال دریافت مناسبت..."
)