package com.example.weatherpocket.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room 의 saved_places 테이블을 표현하는 데이터 클래스이다.
 * 사용자가 관심 도시로 저장한 장소 정보를 보관한다.
 *
 * Entity 는 DB 저장 전용 모델이므로 Domain 모델인 Place 와 분리한다.
 */
@Entity(tableName = "saved_places")
data class PlaceEntity(
    /**
     * Open-Meteo 에서 제공하는 장소 식별자를 기본 키로 사용한다.
     * 동일한 장소를 다시 저장하면 새로운 행을 만들지 않고
     * 기존 장소 데이터를 교체할 수 있다.
     */
    @PrimaryKey
    @ColumnInfo(name = "place_id")
    val id: Long, // 장소 고유 식별자

    @ColumnInfo(name = "name")
    val name: String, // 도시 이름

    @ColumnInfo(name = "country")
    val country: String, // 국가 이름

    @ColumnInfo(name = "country_code")
    val countryCode: String, // 국가 코드

    @ColumnInfo(name = "admin_area")
    val adminArea: String?, // 행정구역 이름, 없을 수 있으므로 nullable

    @ColumnInfo(name = "latitude")
    val latitude: Double, // 위도

    @ColumnInfo(name = "longitude")
    val longitude: Double, // 경도

    @ColumnInfo(name = "time_zone_id")
    val timeZoneId: String, // 시간대 식별자
)
