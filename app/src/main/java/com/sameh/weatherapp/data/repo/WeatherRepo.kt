package com.sameh.weatherapp.data.repo

import com.sameh.weatherapp.data.mappers.toWeatherInfo
import com.sameh.weatherapp.data.remote.WeatherApi
import com.sameh.weatherapp.domain.repo.IWeatherRepo
import com.sameh.weatherapp.domain.util.Resource
import com.sameh.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepo @Inject constructor(
    private val api: WeatherApi
) : IWeatherRepo {

    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat, long
                ).toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error happened !")
        }
    }
}