package com.example.weatherpocket.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    val id: Long, // Open-Meteo 에서 제공하는 city 식별자
    val name: String, // 도시 이름
    val latitude: Double, // 위도
    val longitude: Double, // 경도
    val timezone: String, // 시간대
    val country: String, // 국가 이름
    @SerialName("country_code")
    val countryCode: String, // 국가 코드
    @SerialName("admin1")
    val adminArea: String?, // 행정 구역
)
