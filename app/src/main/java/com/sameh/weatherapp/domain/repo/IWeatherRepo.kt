package com.sameh.weatherapp.domain.repo

import com.sameh.weatherapp.domain.util.Resource
import com.sameh.weatherapp.domain.weather.WeatherInfo

interface IWeatherRepo {

    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}