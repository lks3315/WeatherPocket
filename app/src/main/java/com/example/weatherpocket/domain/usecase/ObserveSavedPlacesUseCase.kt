package com.example.weatherpocket.domain.usecase

import com.example.weatherpocket.domain.model.Place
import com.example.weatherpocket.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow

class ObserveSavedPlacesUseCase(
    private val placeRepository: PlaceRepository, // 장소 데이터
) {
    /**
     * 이 객체를 함수처럼 호출할 수 있게 만든다.
     *
     * 사용 예 : observeSavedPlacesUseCase()
     */
    operator fun invoke(): Flow<List<Place>> {
        return placeRepository.observeSavedPlaces()
    }
}