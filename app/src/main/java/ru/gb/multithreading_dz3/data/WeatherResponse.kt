package ru.gb.multithreading_dz3.data

data class WeatherResponse(
    val main: Main,
    val name: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int
)