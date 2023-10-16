package com.sameh.weatherapp.di

import com.sameh.weatherapp.data.repo.WeatherRepo
import com.sameh.weatherapp.domain.repo.IWeatherRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepo: WeatherRepo
    ): IWeatherRepo
}