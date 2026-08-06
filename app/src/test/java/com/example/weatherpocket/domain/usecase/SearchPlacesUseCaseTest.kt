package com.example.weatherpocket.domain.usecase

import com.example.weatherpocket.domain.model.Place
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals

/**
 * SearchPlacesUseCase 의 검색 규칙을 검증하는 단위 테스트이다.
 *
 * 살재 Retrofit 이나 Room 을 사용하지 않고
 * 테스트용 FakePlacesRepository 를 사용한다.
 */
class SearchPlacesUseCaseTest {
    /**
     * lateinit 은 변수를 선언할 때 바로 값을 넣지 않고
     * setUp() 함수에서 나중에 초기화 하겠다는 뜻
     */
    private lateinit var fakePlaceRepository: FakePlaceRepository // 테스트용 가짜 레포
    private lateinit var searchPlacesUseCase: SearchPlacesUseCase // 테스트 대상 Use case

    /**
     * 각 테스트가 실행되기 전에 호출된다.
     * 테스트마다 새로운 객체를 생성하므로
     * 이전 테스트 값이 다음 테스트에 영향을 주지 않는다.
     */
    @Before
    fun setUp() {
        fakePlaceRepository = FakePlaceRepository()
        searchPlacesUseCase = SearchPlacesUseCase(
            placeRepository =  fakePlaceRepository,
        )
    }

    /**
     * 검색어의 앞뒤 공백을 제거했을 때 한 글자만 남으면
     * 레포지토리를 호출하지 않는 지 검증한다.
     */
    @Test
    fun shortQuerySkipsRepository() = runTest {
        val searchResult = searchPlacesUseCase(
            query = "서", // 공백 제거 후 한 글자가 되는 검색어
        )
        // 검색 결과가 빈 목록인지 확인
        assertTrue(searchResult.isEmpty())

        // 레포의 searchPlaces()가 호출 되지 않아야 한다.
        assertEquals(0, fakePlaceRepository.searchCallCount)
    }

    @Test
    fun queryIsTrimmedBeforeSearch() {
        runTest {
            val seoul = Place(
                id = 1L,
                name = "서울",
                country = "대한민국",
                countryCode = "KR",
                adminArea = "서울특별시",
                latitude = 37.5665, // 위도
                longitude = 126.9780, // 경도
                timeZoneId = "Asia/Seoul", // 시간대 식별자
            )

            val expectedResult = listOf(seoul) // 예상 검색 결과

            // Fake Repository 가 서울을 반환하도록 준비한다.
            fakePlaceRepository.searchResult = expectedResult

            val actualPlaces = searchPlacesUseCase(query = " 서울  ")

            // Repository 에는 공백이 제거된 서울이 전달
            assertEquals("서울", fakePlaceRepository.lastSearchQuery)

            assertEquals(expectedResult, actualPlaces)
        }
    }
}