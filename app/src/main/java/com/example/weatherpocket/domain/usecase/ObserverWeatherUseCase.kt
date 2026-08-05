package com.example.weatherpocket.domain.usecase

import com.example.weatherpocket.domain.model.Weather
import com.example.weatherpocket.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

/**
 * 특정 장소의 저장된 날씨를 관찰하는 Use case
 */
class ObserverWeatherUseCase(
    private val weatherRepository: WeatherRepository, // 날씨 데이터 저장소
) {
    /**
     * Room 에 저장된 특정 장소의 날씨를 Flow 로 관찰한다.
     * 저장된 날씨가 아직 없으면 Flow 가 null 을 전달한다.
     */
    operator fun invoke(
        placeId: Long, // 날씨를 관찰할 장소의 고유 아이디
    ): Flow<Weather?> {
        require(placeId > 0) {
            "장소 아이디는 0보다 커야 합니다."
        }

        return weatherRepository.observeWeather(placeId = placeId,)
    }
}