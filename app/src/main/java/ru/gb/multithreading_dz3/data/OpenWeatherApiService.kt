package ru.gb.multithreading_dz3.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(@Query("q") city: String, @Query("appid") appId: String):
            WeatherResponse
}