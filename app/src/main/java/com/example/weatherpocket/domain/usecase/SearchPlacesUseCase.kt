package com.example.weatherpocket.domain.usecase

import com.example.weatherpocket.domain.model.Place
import com.example.weatherpocket.domain.repository.PlaceRepository

class SearchPlacesUseCase(
    private val placeRepository: PlaceRepository, //
) {
    /**
     * 도시를 검색 한다.
     * 검색어 앞 뒤 공백을 제거하고 검색어가 너무 짧으면 API 를 호출하지 않고 빈 목록을 반환한다.
     */
    suspend operator fun invoke(query: String): List<Place> { // 쿼리: 사용자가 입력한 도시 검색어
        val trimmedQuery = query.trim() // 앞 뒤 공백 제거

        // Open-Meteo 검색 api 는 한 글자 검색을 지원하지 않으므로
        // 두 글자보다 짧으면 불필요한 네트워크 요청을 하지 않는다.
        if (trimmedQuery.length < MINIMUM_QUERY_LENGTH) {
            return emptyList()
        }

        return placeRepository.searchPlaces(
            query = trimmedQuery,
        )
    }

    private companion object {
        const val MINIMUM_QUERY_LENGTH = 2
    }
}