package com.example.weatherpocket.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.weatherpocket.data.local.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

/**
 * saved_places 테이블에 접근하는 DAO 이다.
 * 함수만 정의하면 Room 이 KSP 를 이용하여 실제 DB 접근 코드를 자동으로 생성한다.
 */
@Dao
interface PlacesDao {
    /**
     * 저장된 관심 도시를 이름순으로 관찰한다.
     * saved_places 테이블의 데이터가 변경되면 Flow 가 새로운 장소 목록을 전달한다.
     */
    @Query(
        """SELECT * FROM saved_places ORDER BY name ASC """
    )
    fun observeSavedPlaces(): Flow<List<PlaceEntity>>


    /**
     * 장소 식별자로 저장된 장소 하나를 조회한다.
     * 해당 장소가 존재하지 않으면 null
     */
    @Query(
        """ SELECT * FROM saved_places WHERE place_id = :placeId LIMIT 1 """
    )
    suspend fun getPlace(placeId: Long): PlaceEntity?

    /**
     * 장소를 새로 저장하거나 기존 장소를 갱신한다.
     *
     * 같은 place_id가 없으면 INSERT 수행하고,
     * 같은 place_id가 있으면 UPDATE 수행한다.
     */
    @Upsert
    suspend fun upsertPlace(place: PlaceEntity)

    // 장소 식별자에 해당하는 관심 도시를 삭제한다.
    @Query(
        """DELETE FROM saved_places WHERE place_id = :placeId"""
    )
    suspend fun deletePlace(placeId: Long)

}