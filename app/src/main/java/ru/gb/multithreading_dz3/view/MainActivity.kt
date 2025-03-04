package ru.gb.multithreading_dz3.view

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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

    //    private val compositeDisposable = CompositeDisposable()
    private val viewModel by lazy {
        ViewModelProvider(this)[WeatherViewModel::class.java]
    }
    private val scope = CoroutineScope(Dispatchers.IO)
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        binding.button.setOnClickListener {
            searchJob?.cancel()
            searchJob = scope.launch {
                val respons = viewModel.fetchWeather("Moscow")
                withContext(Dispatchers.Main) {
                    showWeatherData(respons)
                }
            }
        }
    }

    private fun showWeatherData(weather: WeatherResponse) {
        findViewById<TextView>(R.id.weather_text_view).text =
            """
            Город: ${weather.name}
            Температура: ${
                (weather.main.temp - 273).toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
            }°C
            Ощущается как: ${
                (weather.main.feels_like - 273).toBigDecimal().setScale(1, RoundingMode.UP)
                    .toDouble()
            }°C
            Давление: ${weather.main.pressure} hPa
            Влажность: ${weather.main.humidity}%
            """.trimIndent()
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}