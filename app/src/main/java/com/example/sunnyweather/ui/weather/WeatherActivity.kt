package com.example.sunnyweather.ui.weather

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import com.google.android.material.card.MaterialCardView
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    companion object {
        fun actionStart(context: Context, fragment: Fragment, location_lng: String, location_lat: String, place_name: String) {
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", location_lng)
                putExtra("location_lat", location_lat)
                putExtra("place_name", place_name)
            }
            fragment.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        // 设置状态栏沉浸 - Wrong
        // WindowCompat.setDecorFitsSystemWindows(window, false)
        // Biding

        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }

        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }

        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        Log.d("TEMP", "!${viewModel.locationLng}! + !${viewModel.locationLat}!")

        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)

        viewModel.WeatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    private fun showWeatherInfo(weather: Weather) {
        val placeName: TextView = findViewById(R.id.placeName)

        placeName.text = viewModel.placeName

        val realtime = weather.realtime
        val daily = weather.daily

        // now.xml
        val currentTemp: TextView = findViewById(R.id.currentTemp)
        val currentSky: TextView = findViewById(R.id.currentSky)
        val currentAQI: TextView = findViewById(R.id.currentAQI)
        val nowLayout: RelativeLayout = findViewById(R.id.nowLayout)

        val currentTempText = "${realtime.temperature.toInt()} °C"
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"

        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        // forecastLayout
        val forecastLayout: LinearLayout = findViewById(R.id.forecastLayout)

        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]

            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo: TextView = view.findViewById(R.id.dateInfo)
            val skyIcon: ImageView = view.findViewById(R.id.skyIcon)
            val skyInfo: TextView = view.findViewById(R.id.skyInfo)
            val temperatureInfo: TextView = view.findViewById(R.id.temperatureInfo)

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val sky = getSky(skycon.value)
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} °C"

            dateInfo.text = simpleDateFormat.format(skycon.date)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }

        // life_index.xml
        val lifeIndex = daily.lifeIndex
        val coldRiskText: TextView = findViewById(R.id.coldRiskText)
        val dressingText: TextView = findViewById(R.id.dressingText)
        val ultravioletText: TextView = findViewById(R.id.ultravioletText)
        val carWashingText: TextView = findViewById(R.id.carWashingText)
        val weatherLayout: ScrollView = findViewById(R.id.weatherLayout)


        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE

    }
}