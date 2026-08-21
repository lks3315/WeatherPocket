# WeatherPocket

WeatherPocket은 사용자가 원하는 도시를 검색하고 관심 도시로 저장한 뒤, 현재 날씨와 일별 예보를 확인할 수 있도록 만드는 Android 날씨 앱입니다.

이 문서는 다른 PC 또는 새로운 Codex 작업에서 개발을 그대로 이어가기 위한 프로젝트 설명서이자 인수인계 문서입니다. 새 작업을 시작할 때는 먼저 이 문서와 현재 Git 상태를 확인하세요.

## 제품 목표

- Open-Meteo API로 도시를 검색한다.
- 사용자가 선택한 도시를 관심 도시로 저장하고 삭제할 수 있다.
- 저장한 도시의 현재 날씨와 일별 예보를 표시한다.
- 네트워크 응답을 UI에 직접 전달하지 않고 Room을 로컬 단일 데이터 공급원(SSOT)으로 사용한다.
- 네트워크가 불안정하거나 끊겨도 마지막으로 저장된 데이터를 보여줄 수 있는 offline-first 앱을 지향한다.
- Data, Domain, UI 계층을 분리하고 Repository와 Use Case를 통해 의존 방향을 유지한다.
- 각 계층을 가짜 구현으로 대체할 수 있도록 만들어 단위 테스트와 통합 테스트가 가능해야 한다.

## 현재 구현 상태

기준 커밋: `cde8733` (`feat: add application dependency container`)

### 구현 완료

- `Place`, `Weather`, `CurrentWeather`, `DailyForecast`, `WeatherCondition` Domain 모델
- `PlaceRepository`, `WeatherRepository` 인터페이스
- 도시 검색, 저장 도시 관찰, 날씨 관찰 및 새로고침 Use Case 골격
- Room `saved_places` 테이블과 `PlacesDao`
- Room 스키마 버전 1 내보내기
- Open-Meteo Geocoding API Retrofit 인터페이스
- Kotlin Serialization 기반 장소 검색 응답 DTO와 Mapper
- `PlaceRemoteDataSource`
- Room과 Open-Meteo를 연결하는 `OfflineFirstPlaceRepository`
- Room, Retrofit, Repository, Use Case를 연결하는 수동 DI `AppContainer`
- Use Case 단위 테스트, DAO 계측 테스트, Repository 계측 테스트 골격
- 인터넷 권한 선언

### 미완성

- Compose 화면은 기본 `Hello Android!` 템플릿 상태
- 도시 검색 화면과 ViewModel
- 관심 도시 목록 화면과 ViewModel
- 관심 도시 저장/삭제를 UI에서 호출하는 Use Case 또는 연결 코드
- Open-Meteo Forecast API 정의 및 날씨 DTO/Mapper/DataSource
- 날씨 Room Entity, DAO, 관계 및 Migration 전략
- `WeatherRepository` 실제 구현
- 날씨 상세 화면과 새로고침/로딩/오류 상태
- 화면 이동(Navigation)
- 실제 앱 흐름에 대한 UI 테스트
- 오류 모델과 사용자용 오류 메시지

## 반드시 유지할 설계 조건

후속 개발에서는 다음 원칙을 임의로 바꾸지 않습니다. 변경이 필요하면 이유와 영향 범위를 문서와 커밋에 먼저 기록하세요.

1. **Room이 날씨 UI의 단일 데이터 공급원이어야 합니다.**
   - API 응답을 곧바로 UI에 반환하지 않습니다.
   - `refreshWeather(place)`는 API 호출 → Domain/Entity 변환 → Room 저장 순서로 동작합니다.
   - UI는 `observeWeather(placeId)`의 `Flow`만 관찰합니다.

2. **계층 간 모델을 분리합니다.**
   - Remote 계층은 DTO, Local 계층은 Entity, 나머지 앱은 Domain 모델을 사용합니다.
   - DTO나 Entity를 UI와 Domain 계층에 직접 노출하지 않습니다.

3. **Domain 계층은 Android, Retrofit, Room 구현에 의존하지 않습니다.**
   - 데이터 접근은 Repository 인터페이스를 통해서만 수행합니다.
   - 화면 로직은 가능한 한 Use Case를 통해 호출합니다.

4. **Coroutine 취소를 오류로 삼키지 않습니다.**
   - `CancellationException`은 로깅 후 반드시 다시 던집니다.

5. **테스트 가능한 의존성 구조를 유지합니다.**
   - API, DAO, Repository를 생성자 주입합니다.
   - 전역 객체를 기능 코드에서 직접 참조하지 않습니다. 객체 생성은 `AppContainer` 같은 조립 지점에 둡니다.

6. **외부 API는 Open-Meteo를 사용합니다.**
   - 현재 도시 검색 주소: `https://geocoding-api.open-meteo.com/`
   - Geocoding API는 기본적으로 최대 10개, 한국어 결과를 요청합니다.
   - 도시 검색어는 앞뒤 공백을 제거하며 2자 미만이면 API를 호출하지 않습니다.

7. **작업은 작고 검증 가능한 단위로 진행합니다.**
   - 기능별로 구현과 테스트를 함께 추가합니다.
   - 기존 동작과 관련 없는 대규모 리팩터링은 피합니다.

## 기술 스택

- Kotlin 2.0.21
- Android Gradle Plugin 8.10.1
- Jetpack Compose + Material 3
- Kotlin Coroutines / Flow
- Room 2.7.2 + KSP
- Retrofit 3.0.0
- Kotlin Serialization 1.7.3
- JUnit 4 / AndroidX Test / Espresso
- Java/JVM 11
- `minSdk 26`, `targetSdk 35`, `compileSdk 36`

Open-Meteo는 현재 사용하는 API 범위에서 별도 API 키가 필요하지 않습니다. 향후 비밀값이 추가되면 Git에 커밋하지 말고 `local.properties` 또는 안전한 환경 설정을 사용한 뒤 이 문서에는 설정 방법만 기록하세요.

## 프로젝트 구조

```text
app/src/main/java/com/example/weatherpocket/
├── WeatherPocketApplication.kt       # 앱 수준 AppContainer 보관
├── MainActivity.kt                    # 현재 Compose 템플릿 UI
├── di/
│   └── AppContainer.kt                # Room/Retrofit/Repository/Use Case 조립
├── domain/
│   ├── model/                         # Place, Weather 등 순수 Domain 모델
│   ├── repository/                    # 데이터 접근 계약
│   └── usecase/                       # 검색/관찰/새로고침 규칙
├── data/
│   ├── local/                         # Room Database, DAO, Entity, Mapper
│   ├── remote/                        # Retrofit API, DTO, DataSource, Mapper
│   └── repository/                    # Repository 구현
└── ui/
    └── theme/                         # Compose 테마
```

테스트 위치:

```text
app/src/test/          # JVM 단위 테스트
app/src/androidTest/   # Room 및 네트워크 계측/통합 테스트
```

## 다른 PC에서 시작하기

### 1. 저장소 복제

```bash
git clone https://github.com/lks3315/WeatherPocket.git
cd WeatherPocket
```

이 README가 원격 저장소에 올라가려면 현재 PC에서 커밋과 push가 필요합니다.

### 2. Android Studio로 열기

- 프로젝트 루트 `WeatherPocket`을 엽니다.
- Gradle Sync가 끝날 때까지 기다립니다.
- Gradle JDK는 Java 11 이상을 사용합니다. 현재 프로젝트의 Kotlin/JVM 타깃은 11입니다.
- Android SDK Platform 36과 필요한 빌드 도구를 설치합니다.

### 3. 빌드 및 테스트

macOS/Linux:

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

Windows:

```bat
gradlew.bat testDebugUnitTest
gradlew.bat assembleDebug
```

연결된 에뮬레이터나 실제 기기가 있을 때 계측 테스트를 실행합니다.

```bash
./gradlew connectedDebugAndroidTest
```

`PlaceRemoteDataSourceTest`는 실제 Open-Meteo 서버에 접속하므로 인터넷 상태와 외부 서버 상태에 따라 실패할 수 있습니다. 일반적인 회귀 테스트와 분리해서 판단하세요.

## 다음 작업 권장 순서

1. **기존 연결 오류 확인 및 정리**
   - `WeatherPocketApplication`이 실제로 사용되도록 Manifest의 `<application>`에 `android:name=".WeatherPocketApplication"`을 선언합니다.
   - `OfflineFirstPlaceRepositoryTest.searchMapsPlaces()`에 `expectedPlace`와 실제 결과를 비교하는 assertion을 완성합니다.
   - 전체 JVM/계측 테스트와 Debug 빌드를 실행합니다.

2. **장소 기능을 UI까지 완성**
   - 저장/삭제 Use Case를 추가하거나 기존 Repository 호출 정책을 확정합니다.
   - 검색 ViewModel에서 검색어, 로딩, 결과, 오류를 상태로 관리합니다.
   - 검색 결과에서 관심 도시를 저장하고 저장 목록에서 삭제할 수 있게 합니다.
   - 입력 변경 시 이전 검색 작업이 취소되도록 debounce/`flatMapLatest` 또는 명시적 Job 취소를 사용합니다.

3. **날씨 Local 계층 구현**
   - 현재 날씨와 일별 예보를 저장할 Entity 및 DAO를 설계합니다.
   - `placeId`를 기준으로 장소와 날씨 데이터를 연결합니다.
   - `Flow<Weather?>`를 반환하는 Mapper와 DAO 쿼리를 테스트합니다.

4. **날씨 Remote 계층 구현**
   - Open-Meteo Forecast API 인터페이스와 요청 파라미터를 정의합니다.
   - 현재 날씨, 체감온도, 강수확률, 풍속, Weather Code, 일별 최저/최고 온도를 DTO로 받습니다.
   - WMO Weather Code를 `WeatherCondition`으로 변환하며 알 수 없는 값은 `UNKNOWN`으로 처리합니다.
   - 장소의 `timeZoneId`를 고려해 시간 데이터를 변환합니다.

5. **Offline-first WeatherRepository 구현**
   - 조회는 Room `Flow`, 갱신은 API → Room 저장으로 분리합니다.
   - 네트워크 실패 시 기존 Room 데이터가 유지되어야 합니다.
   - 새 데이터 저장은 가능한 한 트랜잭션으로 처리합니다.

6. **날씨 화면과 내비게이션 구현**
   - 저장 도시 목록 → 도시 검색 → 날씨 상세 흐름을 구성합니다.
   - 최초 로딩, 새로고침, 캐시 데이터, 빈 상태, 네트워크 오류를 구분해 표시합니다.

## 현재 확인된 주의사항

- `WeatherPocketApplication.kt`는 구현돼 있지만 Manifest에 `android:name`이 없어 현재 앱 실행 시 생성되지 않을 가능성이 큽니다. 이 상태에서는 `AppContainer` 접근 흐름이 동작하지 않습니다.
- `OfflineFirstPlaceRepositoryTest.searchMapsPlaces()`는 기대값을 만들지만 실제 결과와 비교하지 않아 테스트가 핵심 매핑을 검증하지 못합니다.
- `MainActivity`는 아직 `AppContainer`나 실제 화면에 연결되지 않았습니다.
- `WeatherRepository`는 인터페이스만 있고 구현체 및 DI 등록이 없습니다.
- Room DB는 버전 1입니다. Entity 구조를 바꿀 때 버전과 Migration, 스키마 테스트를 함께 추가해야 합니다. 개발 편의를 위해 파괴적 마이그레이션을 기본값으로 두지 않습니다.
- 기본 패키지/applicationId가 `com.example.weatherpocket`입니다. 배포 전에 실제 소유 도메인 기반 ID로 변경 여부를 결정해야 합니다.
- 기본 템플릿 테스트(`ExampleUnitTest`, `ExampleInstrumentedTest`)는 프로젝트 기능을 검증하지 않으므로 실제 테스트가 충분해지면 정리할 수 있습니다.

## Git 작업 규칙

작업 시작 시:

```bash
git status
git pull --ff-only
```

작업 완료 시 최소한 다음을 확인합니다.

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
git status
```

- 사용자 또는 다른 작업자가 만든 변경을 임의로 되돌리지 않습니다.
- 한 커밋에는 하나의 논리적 변경만 담습니다.
- Room 스키마 JSON은 Migration 추적을 위해 함께 커밋합니다.
- 코드만 보고 확정할 수 없는 제품 요구사항은 추측해 구현하지 말고 사용자에게 확인합니다.

## Codex 인수인계 프롬프트

다른 PC에서 저장소를 연 뒤 다음처럼 시작할 수 있습니다.

> 이 저장소의 README.md를 처음부터 끝까지 읽고 현재 Git 상태와 소스 구현을 대조해줘. README의 설계 조건을 유지하면서 ‘다음 작업 권장 순서’의 첫 번째 미완료 항목부터 진행해줘. 기존 변경은 보존하고, 변경 전후에 관련 테스트와 빌드를 실행해 결과를 알려줘. 요구사항이 문서와 코드만으로 확정되지 않으면 추측해서 구현하지 말고 나에게 질문해줘.

## 문서 갱신 규칙

기능을 완료하거나 설계를 변경할 때 이 README의 다음 항목도 같은 커밋에서 갱신합니다.

- 현재 구현 상태
- 미완성 목록
- 다음 작업 권장 순서
- 현재 확인된 주의사항
- 빌드/실행 절차 및 필요한 환경값

README와 실제 코드가 충돌하면 코드가 현재 상태를 보여주는 증거이지만, 요구사항은 임의로 변경하지 말고 사용자에게 확인하세요.
