package com.example.weatherpocket.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherpocket.data.local.database.WeatherPocketDatabase
import com.example.weatherpocket.data.remote.api.OpenMeteoGeocodingApi
import com.example.weatherpocket.data.remote.datasource.PlaceRemoteDataSource
import com.example.weatherpocket.data.remote.dto.PlaceDto
import com.example.weatherpocket.data.remote.dto.PlaceSearchResponseDto
import com.example.weatherpocket.domain.model.Place
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * OfflineFirstPlaceRepository 가 원격 API 와 Room 데이터를
 * Domain 모델로 올바르게 변환하는 지 검증한다.
 */
@RunWith(AndroidJUnit4::class)
class OfflineFirstPlaceRepositoryTest {

    private lateinit var database: WeatherPocketDatabase
    private lateinit var fakeOpenMeteoGeocodingApi: FakeOpenMeteoGeocodingApi
    private lateinit var offlineFirstPlaceRepository: OfflineFirstPlaceRepository

    // 각 테스트 전에 메모리 Room 과 가짜 API 를 생성한다.
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            WeatherPocketDatabase::class.java
        ).build()

        fakeOpenMeteoGeocodingApi = FakeOpenMeteoGeocodingApi()

        val placeRemoteDataSource = PlaceRemoteDataSource(
            openMeteoGeocodingApi = fakeOpenMeteoGeocodingApi
        )

        offlineFirstPlaceRepository = OfflineFirstPlaceRepository(
            placesDao = database.placeDao(),
            placeRemoteDataSource = placeRemoteDataSource
        )
    }

    /**
     * 각 테스트가 끝나면 메모리 데이터베이스를 닫는다.
     */
    @After
    fun tearDown() {
        database.close()
    }

    /**
     * API 가 변환한 PlaceDto 가 도메인의 Place 변환되는 지 검증한다.
     */
    @Test
    fun searchMapsPlaces() {
        runTest {
            val seoulDto = PlaceDto(
                id = 1835848L,
                name = "서울특별시",
                latitude = 37.566,
                longitude = 126.9784,
                timezone = "Asia/Seoul",
                country = "대한민국",
                countryCode = "KR",
                adminArea = "서울특별시",
            )

            fakeOpenMeteoGeocodingApi.searchResponse = PlaceSearchResponseDto(
                    results = listOf(seoulDto)
                )

            val actualPlaces = offlineFirstPlaceRepository.searchPlaces(query = "Seoul")

            val expectedPlace = Place(
                id = 1835848L,
                name = "서울특별시",
                country = "대한민국",
                countryCode = "KR",
                adminArea = "서울특별시",
                latitude = 37.566,
                longitude = 126.9784,
                timeZoneId = "Asia/Seoul",
            )

            assertEquals("Seoul", fakeOpenMeteoGeocodingApi.lastSearchQuery)
            // last
        }
    }
}

/**
 * 실제 서버에 접속하지 않는 테스트용 Open-Meteo API
 */
private class FakeOpenMeteoGeocodingApi : OpenMeteoGeocodingApi {

    var searchResponse: PlaceSearchResponseDto = PlaceSearchResponseDto() // 테스트에서 반환하도록 지정할 응답
    var lastSearchQuery: String? = null // 마지막으로 전달받은 검색어

    /**
     * 실제 네트워크 요청 대신 테스트에서 지정한 응답을 반환한다.
     */
    override suspend fun searchPlaces(
        query: String,
        count: Int,
        language: String
    ): PlaceSearchResponseDto {
        lastSearchQuery = query

        return searchResponse
    }
}