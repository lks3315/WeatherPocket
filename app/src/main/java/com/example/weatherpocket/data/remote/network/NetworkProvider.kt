package com.example.weatherpocket.data.remote.network

import com.example.weatherpocket.data.remote.api.OpenMeteoGeocodingApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * Retrofit 과 네트워크 API 객체를 생성해 제공한다.
 * 앱 전체에서 Retrofit 객체를 반복해서 생성하지 않고
 * 하나의 객체를 재사용하기 위해 object 로 선언한다.
 */
object NetworkProvider {
    /**
     * JSON 변환 규칙이다.
     * API 가 PlaceDto 에 선언되지 않은 필드를 추가로 보내더라도
     * 오류를 발생시키지 않고 해당 필드를 무시한다.
     */
    private val json = Json {
        ignoreUnknownKeys = true
    }

    // 서버가 전달하는 데이터가 JSON 형식이라는 것을 retrofit 에 알려준다.
    private val contentType = "application/json; charset=utf-8".toMediaType()

    /**
     * retrofit 객체
     *
     * by lazy 를 사용했기 때문에 이 객체가 처음 필요해지는 시점에
     * Retrofit.Builder 코드가 한 번만 실행된다.
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(OpenMeteoGeocodingApi.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    /**
     * Open-Meteo 도시 검색 API 구현체
     * Retrofit 이 OpenMeteoGeocodingApi 인터페이스를 바탕으로
     * 실제 네트워크 요청 코드를 자동 생성
     */
    val openMeteoGeocodingApi: OpenMeteoGeocodingApi by lazy {
        retrofit.create(OpenMeteoGeocodingApi::class.java)
    }
}