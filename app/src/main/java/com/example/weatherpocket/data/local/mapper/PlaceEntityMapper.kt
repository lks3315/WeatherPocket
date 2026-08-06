package com.example.weatherpocket.data.local.mapper

import com.example.weatherpocket.data.local.entity.PlaceEntity
import com.example.weatherpocket.domain.model.Place

/**
 * Room 의 PlaceEntity 를 Domain 모델인 Place 로 변환하는 클래스다.
 * 데이터베이스에서 읽은 Entity 를 앱의 나머지 레이어에 직접 노출하지 않기 위해
 * 변환 과정이 필요하다.
 */
fun PlaceEntity.toDomain(): Place {
    return Place(
        id = id,
        name = name,
        country = country,
        countryCode = countryCode,
        adminArea = adminArea,
        latitude = latitude,
        longitude = longitude,
        timeZoneId = timeZoneId,
    )
}

/**
 * Domain 의 Place 를 Room 의 PlaceEntity 로 변환.
 * 사용자가 장소를 저장할 때 Repository 가 이 함수를 사용한다.
 */
fun Place.toEntity(): PlaceEntity {
    return PlaceEntity(
        id = id,
        name = name,
        country = country,
        countryCode = countryCode,
        adminArea = adminArea,
        latitude = latitude,
        longitude = longitude,
        timeZoneId = timeZoneId,
    )
}