package ru.gb.multithreading_dz3.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather?units=metric&appid=242b3beaf04ba73162007f445d7d5189")
    suspend fun getCurrentWeather(@Query("q") cityName: String): Response<WeatherResponse>
}