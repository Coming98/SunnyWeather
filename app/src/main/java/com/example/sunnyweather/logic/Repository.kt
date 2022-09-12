package com.example.sunnyweather.logic

import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.Dispatcher

object Repository {

    // 为了能将异步获取的数据以响应式编程的方式通知给上一层，通常会返回一个 LiveData 对象
    // 自动构建并返回一个 LiveData 对象，然后在它的代码块中提供一个挂起函数的上下文
    // 这样我们就可以在 liveData() 函数的代码块中调用任意的挂起函数了
    // Dispatchers.IO 表示函数的线程参数类型, 这样代码块中的所有代码就都运行在子线程中了
    // Tips: Android 是不允许在主线程中进行网络请求的，诸如读写数据库之类的本地数据操作也是不建议在主线程中进行的
    // 因此非常有必要在仓库层进行一次线程转换
    fun searchPlaces(query: String) = liveData<Result<List<Place>>>(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        // 发射结果到 Observer
        emit(result)
    }

}