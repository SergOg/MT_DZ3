package ru.gb.multithreading_dz3.viewmodel

import androidx.lifecycle.ViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.multithreading_dz3.data.OpenWeatherApiService

class WeatherViewModel : ViewModel() {
    private val apiKey = "242b3beaf04ba73162007f445d7d5189"
//https://api.openweathermap.org/data/2.5/weather?lon=37.61&lat=55.75&units=metric&appid=242b3beaf04ba73162007f445d7d5189&lang=ru

    private val weatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApiService::class.java)
    }

    suspend fun fetchWeather(city: String) = weatherApi.getCurrentWeather(city, apiKey)
}
