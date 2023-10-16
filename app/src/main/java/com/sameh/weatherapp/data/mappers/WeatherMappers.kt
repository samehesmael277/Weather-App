package com.sameh.weatherapp.data.mappers

import com.sameh.weatherapp.data.remote.WeatherDataDto
import com.sameh.weatherapp.data.remote.WeatherDto
import com.sameh.weatherapp.domain.weather.WeatherData
import com.sameh.weatherapp.domain.weather.WeatherInfo
import com.sameh.weatherapp.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val humidity = humidity[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { indexedWeatherData ->
            indexedWeatherData.data
        }
    }
}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap()
    val currentDate = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if (currentDate.minute < 30) currentDate.hour else currentDate.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}