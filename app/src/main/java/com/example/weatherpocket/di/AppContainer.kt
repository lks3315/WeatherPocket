package com.example.weatherpocket.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.weatherpocket.data.local.database.WeatherPocketDatabase
import com.example.weatherpocket.data.remote.datasource.PlaceRemoteDataSource
import com.example.weatherpocket.data.remote.network.NetworkProvider
import com.example.weatherpocket.data.repository.OfflineFirstPlaceRepository
import com.example.weatherpocket.domain.repository.PlaceRepository
import com.example.weatherpocket.domain.usecase.ObserveSavedPlacesUseCase
import com.example.weatherpocket.domain.usecase.SearchPlacesUseCase

/**
 * 앱에서 사용할 객체들을 생성하고 연결하는 수동 DI 컨테이너이다.
 * Room, Retrofit, Repository, Use Case 가 앱 곳곳에서 중복 생성되지 않도록 한 곳에서 관리한다.
 */
class AppContainer(
    context: Context // Application Context
) {
    /**
     * 앱에서 사용할 Room 데이터베이스
     *
     * Activity Context 대신 Application Context 를 사용하므로 Activity 가
     * 종료 되어도 잘못 참조 되는 문제가 발생 하지 않는다.
     */
    private val database: WeatherPocketDatabase = Room.databaseBuilder(
        context.applicationContext,
        WeatherPocketDatabase::class.java,
        WeatherPocketDatabase.DATABASE_NAME
    ).build()

    private val placeRemoteDataSource = PlaceRemoteDataSource(
        openMeteoGeocodingApi = NetworkProvider.openMeteoGeocodingApi
    )

    // Domain 에는 PlaceRepository 인터페이스로 노출한다.
    // 실제 구현체는 Data 계층의 OfflineFirstPlaceRepository
    val placeRepository: PlaceRepository = OfflineFirstPlaceRepository(
        placesDao = database.placeDao(),
        placeRemoteDataSource = placeRemoteDataSource
    )

    val searchPlaceUseCase = SearchPlacesUseCase(
        placeRepository = placeRepository
    )

    val observeSavedPlacesUseCase = ObserveSavedPlacesUseCase(
        placeRepository = placeRepository
    )

    // 생성자 실행과 속성 초기화가 끝난 뒤 실행된다.
    init {
        Log.d(TAG, "장소 관련 의존성 초기화 완료")
    }

    private companion object {
        const val TAG = "AppContainer"
    }

}