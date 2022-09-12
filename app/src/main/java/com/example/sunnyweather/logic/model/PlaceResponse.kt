package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class Location(val lng: String, val lat: String)

// 由于 JSON 中一些字段的命名可能与 Kotlin 的命名规范不太一致
// 因此这里使用了 @SerializedName 注解的方式，来让 JSON 字段和 Kotlin 字段之间建立映射关系
// 即 Json 中的 formatted_address 字段对应着 kotlin 数据对象的 address 属性
data class Place(val name: String, val location: Location, @SerializedName("formatted_address") val address: String)

data class PlaceResponse(val status: String, val places: List<Place>)
