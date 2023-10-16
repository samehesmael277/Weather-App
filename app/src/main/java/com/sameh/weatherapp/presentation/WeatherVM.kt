package com.sameh.weatherapp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sameh.weatherapp.domain.location.ILocationTracker
import com.sameh.weatherapp.domain.repo.IWeatherRepo
import com.sameh.weatherapp.domain.util.Resource
import com.sameh.weatherapp.utils.toLogD
import com.sameh.weatherapp.utils.toLogE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherVM @Inject constructor(
    private val weatherRepo: IWeatherRepo,
    private val locationTracker: ILocationTracker
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            locationTracker.getCurrentLocation()?.let { location ->
                when (val result =
                    weatherRepo.getWeatherData(location.latitude, location.longitude)) {
                    is Resource.Success -> {
                        "getCurrentLocation Success: ${result.data}".toLogD()
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        "getCurrentLocation Error: ${result.message}".toLogE()
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: kotlin.run {
                "getCurrentLocation null".toLogE()
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't get location, Make sure to grant permission and enable GPS."
                )
            }
        }
    }
}