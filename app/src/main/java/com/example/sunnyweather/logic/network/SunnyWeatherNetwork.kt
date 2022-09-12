package com.example.sunnyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {

    // 创建 PlaceService 这个接口的动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()

    // 发起搜索城市数据的请求
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            // suspendCoroutine 阻塞了当前协程, 必须等待 Lambda 函数体执行完成
            // Lambda 函数体借助 placeService.searchPlaces(query) 的环境调用 enqueue 发起请求并传入回调处理函数
            // 实现数据的获取与处理后使用 协程作用域的 continuation 恢复协程的执行
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}