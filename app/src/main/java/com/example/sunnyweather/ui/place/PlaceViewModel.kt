package com.example.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.PlaceResponse

class PlaceViewModel: ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    // 对界面上显示的城市数据进行缓存
    val placeList = ArrayList<PlaceResponse.Place>()

    // switchMap 监听 searchLiveData 的变化实现对 placeLiveData 的更新获取
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    fun savePlace(place: PlaceResponse.Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

}