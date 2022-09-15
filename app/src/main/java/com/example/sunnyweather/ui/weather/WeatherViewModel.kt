package com.example.sunnyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.PlaceResponse

class WeatherViewModel: ViewModel() {

    private val locationLiveData = MutableLiveData<PlaceResponse.Location>()

    // 界面需要用到的相关数据
    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    val WeatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = PlaceResponse.Location(lng, lat)
    }

}