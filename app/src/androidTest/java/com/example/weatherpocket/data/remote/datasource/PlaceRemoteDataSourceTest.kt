package com.example.weatherpocket.data.remote.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherpocket.data.remote.network.NetworkProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 실제 Open Meteo 서버와 통신하는 통합 테스트
 * 외부 서버와 인터넷 연결 상태에 영향을 받을 수 있으므로
 * 일반적인 단위 테스트와 구분해서 사용한다.
 */
@RunWith(AndroidJUnit4::class)
class PlaceRemoteDataSourceTest {
    /**
     * '서울'을 검색했을 때 한 개 이상의 도시가 반환되는 지 확인
     */
    @Test
    fun searchReturnPlaces() {
        runBlocking {
            val placeRemoteDataSource = PlaceRemoteDataSource(
                openMeteoGeocodingApi = NetworkProvider.openMeteoGeocodingApi,
            )

            val placeDtos = placeRemoteDataSource.searchPlace(
                query = "Seoul",
            )

            // 검색 결과가 비어 있지 않은 지 확인한다.
            assertTrue(placeDtos.isNotEmpty())

            // 테스트 결과 창에서 첫 번째 검색 결과를 확인하기 위한 출력이다.
            val firstPlaceDto = placeDtos.first()

            println(
                "검색 결과: ${firstPlaceDto.name}, " +
                        "${firstPlaceDto.country}, " +
                        "${firstPlaceDto.latitude}, " +
                        "${firstPlaceDto.longitude}",
            )
        }
    }
}