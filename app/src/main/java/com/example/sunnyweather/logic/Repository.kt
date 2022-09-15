package com.example.sunnyweather.logic

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.liveData
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.logic.model.PlaceResponse
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {

    fun savePlace(place: PlaceResponse.Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    // 为了能将异步获取的数据以响应式编程的方式通知给上一层，通常会返回一个 LiveData 对象
    // 自动构建并返回一个 LiveData 对象，然后在它的代码块中提供一个挂起函数的上下文
    // 这样我们就可以在 liveData() 函数的代码块中调用任意的挂起函数了
    // Dispatchers.IO 表示函数的线程参数类型, 这样代码块中的所有代码就都运行在子线程中了
    // Tips: Android 是不允许在主线程中进行网络请求的，诸如读写数据库之类的本地数据操作也是不建议在主线程中进行的
    // 因此非常有必要在仓库层进行一次线程转换
    // fun searchPlaces(query: String) = liveData<Result<List<PlaceResponse.Place>>>(Dispatchers.IO) {
    //     val result = try {
    //         val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
    //         if (placeResponse.status == "ok") {
    //             val places = placeResponse.places
    //             Result.success(places)
    //         } else {
    //             Result.failure(RuntimeException("response status is ${placeResponse.status}"))
    //         }
    //     } catch (e: Exception) {
    //         Result.failure(e)
    //     }
    //     // 发射结果到 Observer
    //     emit(result)
    // }
    // 统一接口处理 try catch
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }


    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            // 并发执行 同时响应
            // async 必须在协程内调用 因此使用 coroutineScope 函数创建协程作用域
            val deferredRealtime = async { SunnyWeatherNetwork.getRealtimeWeather(lng, lat) }
            val deferredDaily = async { SunnyWeatherNetwork.getDailyWeather(lng, lat) }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    java.lang.RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                        "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    // 统一的入口函数中进行封装，使得只要进行一次 try catch 处理就行了
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure<T>(e)
        }
        emit(result)
    }


}