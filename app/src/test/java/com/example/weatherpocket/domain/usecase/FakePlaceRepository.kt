package com.example.weatherpocket.domain.usecase

import com.example.weatherpocket.domain.model.Place
import com.example.weatherpocket.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * PlaceRepository의 테스트용 가짜 구현체다.
 *
 * Fake 실제 네트워크나 데이터베이스를 사용하지 않는다.
 * 테스트가 원하는 결과와 호출 정보를 메모리에 저장한다.
 */
class FakePlaceRepository : PlaceRepository {

    var searchCallCount: Int = 0 // searchPlaces()가 호출된 횟수

    var lastSearchQuery: String? = null // 마지막으로 전달받은 검색어

    var searchResult: List<Place> = emptyList() // 테스트에서 지정할 검색 결과

    /**
     * 실제 도시 검색 대신 테스트가 지정한 searchResult를 반환한다.
     */
    override suspend fun searchPlaces(
        query: String, // Use Case가 전달한 도시 검색어
    ): List<Place> {
        searchCallCount += 1
        lastSearchQuery = query

        return searchResult
    }

    /**
     * 이번 테스트에서는 사용하지 않는 함수다.
     *
     * 인터페이스를 구현하려면 모든 추상 함수를 구현해야 하므로
     * 비어 있는 도시 목록을 한 번 전달하는 Flow 를 반환한다.
     */
    override fun observeSavedPlaces(): Flow<List<Place>> {
        return flowOf(emptyList())
    }

    // 아래 함수들은 이번 검색 테스트에서 사용하지 않는다.
    override suspend fun savePlace(
        place: Place, // 저장할 장소
    ) {
        // 테스트에서 사용하지 않으므로 아무 작업도 하지 않는다.
    }

    override suspend fun deletePlace(
        placeId: Long, // 삭제할 장소 식별자
    ) {
        // 테스트에서 사용하지 않으므로 아무 작업도 하지 않는다.
    }

    override suspend fun getPlace(
        placeId: Long, // 조회할 장소 식별자
    ): Place? {
        // 테스트에서 사용하지 않으므로 null 반환한다.
        return null
    }
}