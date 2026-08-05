package com.example.weatherpocket.domain.model

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

data class Weather(
    val placeId: Long,
    val current: CurrentWeather,
    val dailyForecasts: List<DailyForecast>,
    val updatedAt: Instant
)

data class CurrentWeather(
    val measureAt: LocalDateTime,
    val temperatureCelsius: Double,
    val apparentTemperatureCelsius: Double,
    val precipitationProbabilityPercent: Int,
    val windSpeedKmh: Double,
    val condition: WeatherCondition,
) {
    init {
        require(precipitationProbabilityPercent in 0..100)
        require(windSpeedKmh >= 0.0)
    }
}

data class DailyForecast(
    val date: LocalDate,
    val minimumTemperatureCelsius: Double,
    val maximumTemperatureCelsius: Double,
    val precipitationProbabilityPercent: Int,
    val condition: WeatherCondition,
) {
    init {
        require(minimumTemperatureCelsius <= maximumTemperatureCelsius)
        require(precipitationProbabilityPercent in 0..100)
    }
}

enum class WeatherCondition {
    CLEAR,
    PARTLY_CLOUDY,
    CLOUDY,
    FOG,
    DRIZZLE,
    RAIN,
    SNOW,
    THUNDERSTORM,
    UNKNOWN,
}
