package com.example.weatherpocket.data.remote.datasource

import com.example.weatherpocket.data.remote.api.OpenMeteoGeocodingApi
import com.example.weatherpocket.data.remote.dto.PlaceDto

class PlaceRemoteDataSource(
    private val openMeteoGeocodingApi: OpenMeteoGeocodingApi
) {
    // Open meteo 의 도시 목록을 검색한다.
    suspend fun searchPlace(
        query: String,
    ): List<PlaceDto> {
        val response = openMeteoGeocodingApi.searchPlaces(query = query)
        return response.results
    }
}