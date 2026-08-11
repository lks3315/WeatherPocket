package com.example.weatherpocket.data.remote.mapper

import com.example.weatherpocket.data.remote.dto.PlaceDto
import com.example.weatherpocket.domain.model.Place

private const val DEFAULT_TIME_ZONE_ID = "UTC" // 시간대가 없을 때 사용할 기본값

/**
 * 네트워크의 PlaceDto 를 Domain 모델인 Place 로 변환한다.
 * Domain 계층이 Retrofit DTO 에 의존하지 않도록 변환 과정이 필요하다.
 */
fun PlaceDto.toPlace(): Place {
    return Place(
        id = id,
        name = name,
        country = country.orEmpty(),
        countryCode = countryCode.orEmpty(),
        adminArea = adminArea,
        latitude = latitude,
        longitude = longitude,
        timeZoneId = timezone ?: DEFAULT_TIME_ZONE_ID,
    )
}
