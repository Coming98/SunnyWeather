package com.example.sunnyweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.PlaceResponse
import com.google.gson.Gson

object PlaceDao {

    private fun sharedPreferences() = SunnyWeatherApplication.context.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)

    fun isPlaceSaved() = sharedPreferences().contains("place")

    fun savePlace(place: PlaceResponse.Place) {
        sharedPreferences().edit() {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): PlaceResponse.Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, PlaceResponse.Place::class.java)
    }


}