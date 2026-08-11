package com.example.weatherpocket.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Open-Meteo 도시 검색 API 의 전체 응답을 표현하는 DTO 다.
 *
 * API 가 반환한 JSON 데이터를 Kotlin 객체로 변환할 때 사용한다.
 *
 * DTO 는 Data Transfer Object 의 약자로,
 * 네트워크를 통해 전달 받는 데이터의 형태를 나타낸다.
 */
@Serializable
data class PlaceSearchResponseDto(
    val results: List<PlaceDto> = emptyList() // 검색 도시 목록
)
