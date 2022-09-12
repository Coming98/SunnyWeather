package com.example.sunnyweather.logic.network

import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET()
    fun searchPlaces(@Query("query") query: String): Call<PlaceRes>

}