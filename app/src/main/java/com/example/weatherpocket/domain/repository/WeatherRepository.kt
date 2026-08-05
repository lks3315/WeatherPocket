package com.example.weatherpocket.domain.repository

import com.example.weatherpocket.domain.model.Place
import com.example.weatherpocket.domain.model.Weather
import kotlinx.coroutines.flow.Flow

/**
 * 날씨 데이터에 접근하기 위한 규칙을 정의한다.
 *
 * ViewModel 이나 Use Case 는 Retrofit, Room 을 직접 사용하지 않고 이 인터페이스를 통해서만 날씨 데이터에 접근
 */
interface WeatherRepository {
    /**
     * 특정 장소의 저장된 날씨를 지속적으로 관찰한다.
     *
     * UI는 네트워크 응답을 직접 사용하지 않고
     * Room 저장된 날씨만 이 Flow 통해 관찰한다.
     *
     * 저장된 날씨가 없다면 null 전달
     */

    fun observeWeather(
        placeId: Long, // 날씨를 관찰할 장소의 고유 식별자
    ): Flow<Weather?>


    /**
     * 특정 장소의 최신 날씨를 외부 API 에서 가져온다.
     *
     * 나중에 작성할 구현체는 다음 순서로 동작함.
     *
     * 1. Place 위도와 경도로 Retrofit API 호출
     * 2. API 응답을 Domain 모델로 변환
     * 3. 변환된 데이터를 Room 데이터베이스에 저장
     * 4. Room 의 변경 사항이 observeWeather() 로 전달
     *
     * 반환 값이 없는 이유는 UI 가 네트워크 응답을 직접 사용하지 않고
     * Room 을 단일 공급원으로 사용하도록 만들기 위함
     */
    suspend fun refreshWeather(
        place: Place, // 최신 날씨를 가져올 장소 정보
    )
}