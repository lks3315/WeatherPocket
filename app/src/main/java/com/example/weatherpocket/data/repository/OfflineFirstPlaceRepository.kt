package com.example.weatherpocket.data.repository

import android.util.Log
import com.example.weatherpocket.data.local.dao.PlacesDao
import com.example.weatherpocket.data.local.mapper.toDomain
import com.example.weatherpocket.data.local.mapper.toEntity
import com.example.weatherpocket.data.remote.datasource.PlaceRemoteDataSource
import com.example.weatherpocket.data.remote.mapper.toPlace
import com.example.weatherpocket.domain.model.Place
import com.example.weatherpocket.domain.repository.PlaceRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * PlaceRepository 의 실제 구현체다.
 * 도시 검색은 Open-Meteo 의 도시 검색 API 를 사용하고
 * 관심 도시 저장과 조회는 Room 이용
 *
 * Domain 계층은 이 클래스의 구체적인 동작을 알지 못하고
 * PlaceRepository 인터페이스를 통해서만 접근한다.
 */
class OfflineFirstPlaceRepository(
    private val placesDao: PlacesDao, // Room 의 관심 도시 DAO
    private val placeRemoteDataSource: PlaceRemoteDataSource, // 도시 검색 데이터
): PlaceRepository {

    private companion object {
        const val TAG = "OfflineFirstPlaceRepository"
    }

    /**
     * Room 에 저장된 관심 도시 목록을 관찰한다.
     *
     * PlaceEntity 는 데이터 계층의 모델이므로
     * 도메인 모델인 Place 로 변환해서 반환한다.
     */
    override fun observeSavedPlaces(): Flow<List<Place>> {
        return placesDao.observeSavedPlaces()
            .map { placeEntities ->
                val places = placeEntities.map { placeEntity ->
                    placeEntity.toDomain()
                }

                Log.d(TAG, "저장 도시 목록 변경: count=${places.size}")
                places // 코틀린 람다는 마지막 표현식의 값을 반환
            }
    }

    /**
     * Open-Meteo 에서 도시를 검색한다.
     *
     * PlaceDto 는 네트워크 전용 모델이므로
     * Domain 모델인 Place 로 변환한다.
     */
    override suspend fun searchPlaces(query: String): List<Place> {
        Log.d(TAG, "도시 검색 시작: query=$query")

        return try {
            val placeDtos = placeRemoteDataSource.searchPlace(query = query)
            val places = placeDtos.map { placeDto ->
                placeDto.toPlace()
            }
            places
        } catch (exception: CancellationException) {
            // 화면 이동이나 새로운 검색으로 코루틴이 취소된 경우
            Log.d(TAG, "도시 검색 취소: query=$query")

            // 코루틴 취소는 정상적인 제어 흐름이므로 다시 전달
            throw exception
        } catch (exception: Exception) {
            Log.e(TAG, "도시 검색 실패: query=$query", exception)
            throw exception
        }
    }

    /**
     * 선택한 도시를 Room 에 저장한다.
     */
    override suspend fun savePlace(place: Place) {
        Log.d(TAG, "도시 저장 시작: placeId=${place.id}, name=${place.name}")
        val placeEntity = place.toEntity()
        placesDao.upsertPlace(placeEntity = placeEntity)
        Log.d(TAG, "도시 저장 완료: placeId=${place.id}")
    }

    override suspend fun deletePlace(placeId: Long) {
        Log.d(TAG, "도시 삭제 시작: placeId=$placeId")
        placesDao.deletePlace(placeId = placeId)
        Log.d(TAG, "도시 삭제 완료: placeId=$placeId")
    }

    override suspend fun getPlace(placeId: Long): Place? {
        val placeEntity = placesDao.getPlace(placeId = placeId)
        val place = placeEntity?.toDomain()
        Log.d(TAG, "도시 조회 완료: placeId=$placeId, found=${place != null}")
        return place
    }

}