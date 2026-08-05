package com.example.weatherpocket.domain.repository

import com.example.weatherpocket.domain.model.Place
import kotlinx.coroutines.flow.Flow

/**
 * 장소 데이터에 접근하기 위한 규칙을 정의한다.
 *
 * Domain 레이어는 실제 데이터가 Room에서 오는지,
 * Retrofit에서 오는 지 알 필요가 없다.
 *
 * 실제 구현은 나중에 Data 레이어에서 작성하기
 */
interface PlaceRepository {
    /**
     * Room에 저장된 관심 도시 목록을 지속적으로 관찰한다.
     *
     * 저장 또는 삭제로 데이터가 변경되면
     * Flow가 새로운 도시 목록을 전달한다.
     */
    fun observeSavedPlaces(): Flow<List<Place>>

    // 사용자가 입력한 검색어로 도시를 검색한다.
    // 나중에 Open-Meteo의 도시 검색 API를 호출하게 된다.
    suspend fun searchPlaces(
        query: String, // 유저가 입력한 도시 검색어
    ): List<Place>

    // 선택한 도시를 관심 도시로 저장
    suspend fun savePlace(place: Place)

    // 관심 도시 목록에서 삭제
    suspend fun deletePlace(placeId: Long)

    // 고유 식별자를 이용하여 특정 장소 하나를 조회
    // 해당 장소가 저장되어 있지 않으면 null
    suspend fun getPlace(placeId: Long): Place?
}