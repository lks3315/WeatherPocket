package com.example.weatherpocket.data.remote.api

import com.example.weatherpocket.data.remote.dto.PlaceSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Open-Meteo 도시 검색 API 를 호출하는 Retrofit 인터페이스다.
 *
 * Retrofit 이 이 인터페이스의 구현체를 자동으로 생성한다.
 */
interface OpenMeteoGeocodingApi {

    /**
     * 사용자가 입력한 검색어로 도시를 검색한다.
     */
    @GET("v1/search")
    suspend fun searchPlaces(
        @Query("name")
        query: String, // 사용자가 입력한 도시 검색어

        @Query("count")
        count: Int = 10, // 최대 검색 결과 수

        @Query("language")
        language: String = "ko", // 검색 결과 언어
    ): PlaceSearchResponseDto

    companion object {
        /**
         * Retrofit 에서 사용할 Open-Meteo 기본 주소다.
         * Retrofit 의 baseUrl 은 반드새 슬래시(/)로 끝내야 한다.
         */
        const val BASE_URL = "https://geocoding-api.open-meteo.com/"
    }
}