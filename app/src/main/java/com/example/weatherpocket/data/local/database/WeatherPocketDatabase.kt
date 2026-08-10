package com.example.weatherpocket.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherpocket.data.local.dao.PlacesDao
import com.example.weatherpocket.data.local.entity.PlaceEntity

/**
 * WeatherPocket 앱에서 사용할 Room 데이터베이스이다.
 * DB 가 관리할 Entity 와 DAO 를 Room 에 등록한다.
 * 실제 구현 클래스는 Room 이 KSP 를 통해 자동으로 생성한다.
 */
@Database(
    entities = [ // @Database 가 관리할 Entity 목록, 현재는 PlaceEntity 하나만 등록함
        PlaceEntity::class,
    ],
    version = 1, // 데이터베이스 구조의 버전
    exportSchema = true // Room 이 데이터베이스 구조를 JSON 파일로 저장하도록 함. 이 파일은 이후 Migration 을 만들고 테스트할 때 사용함.
)
abstract class WeatherPocketDatabase : RoomDatabase() {
    /**
     * saved_places 테이블에 접근할 수 있는 PlaceDao 를 제공한다.
     * 구현 코드는 Room 이 자동으로 생성하기 때문에 abstract 선언으로 만들어둔다.
     */
    abstract fun placeDao(): PlacesDao

    companion object {
        // 기기에 생성 될 SQLite 데이터베이스 파일 이름
        const val DATABASE_NAME = "weather_pocket.db"
    }
}