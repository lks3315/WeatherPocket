package com.example.weatherpocket.domain.model

// Location 대신 Place 사용하는 이유는 Android의 Location api와 혼동을 피하기 위함
data class Place(
    val id: Long,
    val name: String,
    val country: String,
    val countryCode: String,
    val adminArea: String,
    val latitude: Double,
    val longitude: Double,
    val timeZoneId: String,
) {
    init {
        // Domain 레이어 안으로 유효하지 않는 장소가 들어오는 것을 막는 규칙
        require(name.isNotBlank()) {
            "Place name must not be blank."
        }
        require(latitude in -90.0..90.0) {
            "Latitude must be between -90 and 90."
        }
        require(longitude in -180.0..180.0) {
            "Longitude must be between -180 and 180."
        }
    }
}
