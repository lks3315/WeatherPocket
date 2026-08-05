package com.example.weatherpocket.domain.usecase

import com.example.weatherpocket.domain.model.Place
import com.example.weatherpocket.domain.repository.WeatherRepository

/**
 * 특정 장소의 최신 날씨를 가져오는 Use case
 */
class RefreshWeatherUseCase(
    private val weatherRepository: WeatherRepository, // 날씨 데이터 저장소
) {
    /**
     * Repository 에 최신 날씨 갱신을 요청
     *
     * Repository 구현체는 나중에 다음 작업을 수행
     * 1. Retrofit 으로 최신 날씨를 가져온다.
     * 2. API 응답을 앱 모델로 변환한다.
     * 3. 변환된 날씨를 Room 에 저장한다.
     */
    suspend operator fun invoke(
        place: Place,
    ) {
        weatherRepository.refreshWeather(place = place)
    }
}