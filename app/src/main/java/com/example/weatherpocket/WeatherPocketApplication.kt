package com.example.weatherpocket

import android.app.Application
import android.util.Log
import com.example.weatherpocket.di.AppContainer

/**
 * WeatherPocket 앱 프로세스에서 가장 먼서 생성되는 Application
 * 앱 전체에서 사용할 AppContainer 를 보관한다.
 */
class WeatherPocketApplication: Application() {
    // AppContainer 가 처음 필요한 시점에 한 번만 생성한다.
    val appContainer: AppContainer by lazy {
        Log.d(TAG, "AppContainer 초기화")
        AppContainer(context = applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "WeatherPocket onCreate")
    }

    private companion object {
        const val TAG = "WeatherPocketApplication"
    }
}