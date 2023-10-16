package com.sameh.weatherapp.data.remote

import com.sameh.weatherapp.app.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(Constants.GET_WEATHER_DATA_REQUEST_URL)
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): WeatherDto
}