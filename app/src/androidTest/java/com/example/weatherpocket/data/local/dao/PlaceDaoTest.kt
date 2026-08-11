package com.example.weatherpocket.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherpocket.data.local.database.WeatherPocketDatabase
import com.example.weatherpocket.data.local.entity.PlaceEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * PlaceDao 의 Room 데이터베이스 동작을 검증하는 계측 테스트다.
 * 실제 기기 또는 에뮬레이터에서 Android SQLite 를 사용하지만,
 * 테스트가 끝나면 사라지는 메모리 데이터베이스를 사용한다.
 */
@RunWith(AndroidJUnit4::class)
class PlaceDaoTest {
    // 테스트에서 사용할 메모리 기반 Room 데이터베이스
    private lateinit var database: WeatherPocketDatabase

    // 테스트할 DAO
    private lateinit var placesDao: PlacesDao

    // 각각의 테스트가 시작되기 전에 새로운 데이터베이스를 생성한다.
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, WeatherPocketDatabase::class.java
        ).build()
        placesDao = database.placeDao()
    }

    /**
     * 각각의 테스트가 끝난 후 데이터베이스를 닫는다.
     * 테스트마다 데이터베이스를 새로 생성하므로
     * 이전 테스트의 데이터가 다음 테스트에 영향을 주지 않는다.
     */
    @After
    fun tearDown() {
        database.close()
    }

    /**
     * @Upsert 로 저장한 장소를 다시 조회할 수 있는지 검증한다.
     */
    @Test
    fun upsertStoresPlace() {
        runTest {
            val seoul = createSeoul()

            placesDao.upsertPlace(
                placeEntity = seoul,
            )

            val savedPlace = placesDao.getPlace(
                placeId = seoul.id,
            )

            assertEquals(seoul, savedPlace)
        }
    }

    /**
     * observeSavedPlaces()가 장소를 이름순으로 반환하는 지 검증한다.
     */
    @Test
    fun placesAreOrderedByName() {
        runTest {
            val seoul = createSeoul()
            val busan = createBusan()

            // 서울을 먼저 저장해도 SQL 의 ORDER BY 때문에
            // 결과는 부산, 서울 순서로 반환되어야 한다.
            placesDao.upsertPlace(
                placeEntity = seoul,
            )
            placesDao.upsertPlace(
                placeEntity = busan,
            )

            // Flow가 처음 전달하는 장소 목록을 가져온다.
            val savedPlaces = placesDao
                .observeSavedPlaces()
                .first()

            val expectedPlaces = listOf(
                busan,
                seoul,
            )

            assertEquals(expectedPlaces, savedPlaces)
        }
    }

    /**
     * 장소를 삭제한 후 다시 조회하면 null이 반환되는지 검증한다.
     */
    @Test
    fun deleteRemovesPlace() {
        runTest {
            val seoul = createSeoul()

            placesDao.upsertPlace(
                placeEntity = seoul,
            )

            placesDao.deletePlace(
                placeId = seoul.id,
            )

            val deletedPlace = placesDao.getPlace(
                placeId = seoul.id,
            )

            assertNull(deletedPlace)
        }
    }

    /**
     * 테스트에서 사용할 서울 Entity를 생성한다.
     */
    private fun createSeoul(): PlaceEntity {
        return PlaceEntity(
            id = 1L,
            name = "서울",
            country = "대한민국",
            countryCode = "KR",
            adminArea = "서울특별시",
            latitude = 37.5665,
            longitude = 126.9780,
            timeZoneId = "Asia/Seoul",
        )
    }

    /**
     * 테스트에서 사용할 부산 Entity를 생성한다.
     */
    private fun createBusan(): PlaceEntity {
        return PlaceEntity(
            id = 2L,
            name = "부산",
            country = "대한민국",
            countryCode = "KR",
            adminArea = "부산광역시",
            latitude = 35.1796,
            longitude = 129.0756,
            timeZoneId = "Asia/Seoul",
        )
    }
}