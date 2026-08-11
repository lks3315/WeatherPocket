// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // 하위 모듈에서 KSP 플러그인을 사용할 수 있도록 등록한다.
    alias(libs.plugins.ksp) apply false
    // 하위 모듈에서 Room Gradle 플러그인을 사용할 수 있도록 등록한다.
    alias(libs.plugins.androidx.room) apply false
    // 하위 모듈에서 Kotlin Serialization을 사용할 수 있도록 등록한다.
    alias(libs.plugins.kotlin.serialization) apply false
}