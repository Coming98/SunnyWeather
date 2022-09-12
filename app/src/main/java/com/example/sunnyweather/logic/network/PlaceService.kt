package com.example.sunnyweather.logic.network

import com.example.sunnyweather.R
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    // 输入查询字段 返回与该字段相关的多个城市（地点）信息
    @GET("v2/place?token=${SunnyWeatherApplication.CY_TOKEN}&lang=zh_CN")
    // 返回值必须声明成 Retrofit 中内置的 Call 类型
    // @Query 用于配置 Get 请求中的参数; 其中 token 与 lang 为静态参数可以直接写入注解中
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

}