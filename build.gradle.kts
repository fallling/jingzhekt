// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.devtools.ksp)
    id("com.google.dagger.hilt.android") version "2.57.2" apply false
    alias(libs.plugins.compose.compiler) apply false
}
//
//buildscript{
//    repositories {
//        google()
//        mavenCentral()
//    }
//    dependencies {
//        classpath(libs.hilt.android.gradle.plugin)
//    }
//}