import java.util.Properties

plugins {
    alias(libs.plugins.devToolsKsp)
    alias(libs.plugins.daggerHiltAndroid)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias (libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "be.business.newsapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "be.business.newsapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    val properties = Properties()
    val propertiesFile = rootProject.file("newsapp.properties")
    if (propertiesFile.canRead()) {
        properties.load(propertiesFile.inputStream())
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += setOf(
                "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
            )
        }
    }
    flavorDimensions += listOf("environment")
    productFlavors {
        create("dev") {
            dimension = "environment"
            buildConfigField(type = "String", name = "env", value = "\"dev\"")
            buildConfigField("String", "NEWS_API_KEY", "\"${properties.getProperty("NEWS_API_KEY")}\"")
        }
        create("staging") {
            dimension = "environment"
            buildConfigField(type = "String", name = "env", value = "\"stg\"")
        }
        create("production") {
            dimension = "environment"
            buildConfigField(type = "String", name = "env", value = "\"prod\"")
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
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.slf4j.android)
    implementation(libs.ktor.client.resources)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation 3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)
    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.logging.interceptor)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.moshi.kotlin.codegen)
    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.cio)

    //Coil
    implementation(libs.coil)
    implementation(libs.coil.compose)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.lifecycle.viewmodel)
    // Datastore
    implementation(libs.androidx.datastore.preferences)
    // Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
}