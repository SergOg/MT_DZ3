package ru.gb.multithreading_dz3.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.gb.multithreading_dz3.R
import ru.gb.multithreading_dz3.data.WeatherResponse
import ru.gb.multithreading_dz3.databinding.ActivityMainBinding
import ru.gb.multithreading_dz3.viewmodel.WeatherViewModel
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[WeatherViewModel::class.java]
    }
//    private val scope = CoroutineScope(Dispatchers.IO)
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                val respons = viewModel.fetchWeather("Moscow")
                withContext(Dispatchers.Main) {
                    showWeatherData(respons)
                }
            }
        }
    }

    private fun showWeatherData(weather: WeatherResponse) {
        val city = getString(R.string.city)
        val temperature = getString(R.string.temperature)
        val feels_like = getString(R.string.feels_like)
        val pressure = getString(R.string.pressure)
        val humidity = getString(R.string.humidity)
        findViewById<TextView>(R.id.weather_text_view).text =
            """
            ${city} ${weather.name}
            ${temperature} ${
                (weather.main.temp - 273).toBigDecimal().setScale(1, RoundingMode.UP).toDouble()}°C
            ${feels_like} ${
                (weather.main.feels_like - 273).toBigDecimal().setScale(1, RoundingMode.UP).toDouble()}°C
            ${pressure} ${weather.main.pressure} hPa
            ${humidity} ${weather.main.humidity}%
            """.trimIndent()
    }

    override fun onDestroy() {
//        scope.cancel()
        super.onDestroy()
    }
}