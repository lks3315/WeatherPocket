plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Room 컴파일러가 Kotlin 코드를 분석할 수 있게 한다.
    alias(libs.plugins.ksp)
    // Room 데이터베이스 스키마 파일을 관리한다.
    alias(libs.plugins.androidx.room)
    // 하위 모듈에서 Kotlin Serialization 을 사용할 수 있도록 등록한다.
    alias(libs.plugins.kotlin.serialization) apply false
}

android {
    namespace = "com.example.weatherpocket"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.weatherpocket"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    /**
     * Room 데이터베이스 구조를 JSON 파일로 저장한다.
     *
     * 이 파일은 나중에 데이터베이스 버전을 올릴 때
     * 이전 구조와 새로운 구조를 비교하는 데 사용한다.
     */
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // compose view-model
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.kotlinx.coroutines.android)

    // Room 기본 기능
    implementation(libs.androidx.room.runtime)
    // Room에서 suspend 함수와 Flow를 사용하기 위한 확장 기능
    implementation(libs.androidx.room.ktx)
    // Room 어노테이션을 분석하고 구현 코드를 생성한다.
    ksp(libs.androidx.room.compiler)
    // 실제 기기 또는 에뮬레이터에서 Room을 테스트할 때 사용한다.
    androidTestImplementation(libs.androidx.room.testing)

    // Retrofit 기본 HTTP 통신 기능
    implementation(libs.retrofit.core)
    // Retrofit 응답 JSON을 Kotlin 객체로 변환
    implementation(libs.retrofit.converter.kotlinx.serialization)
    // Kotlin의 JSON 직렬화와 역직렬화 기능
    implementation(libs.kotlinx.serialization.json)
}