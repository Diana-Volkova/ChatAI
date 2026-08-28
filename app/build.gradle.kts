import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}


val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

val backendIp = localProperties.getProperty("backendIp")
    ?: error("backendIp is not defined in local.properties")

android {
    namespace = "com.example.chatai"
    compileSdk = 37

    buildFeatures {
        compose = true
        resValues = true
    }

    defaultConfig {
        applicationId = "com.example.chatai"
        minSdk = 29
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        resValue("string", "backend_ip", backendIp)
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

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.material.icons)

    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}