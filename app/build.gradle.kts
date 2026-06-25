import com.android.build.api.dsl.Packaging
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.house_rental_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.house_rental_app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        val localProps = Properties().apply {
            val localFile = rootProject.file("local.properties")
            if (localFile.exists()) {
                localFile.inputStream().use { load(it) }
            }
        }
        val yandexMapkitKey = (localProps.getProperty("yandex.mapkit.api.key"))
            ?: (project.findProperty("yandex.mapkit.api.key") as String?)
            ?: System.getenv("YANDEX_MAPKIT_API_KEY")
            ?: ""
        manifestPlaceholders["YANDEX_MAPKIT_API_KEY"] = yandexMapkitKey
        buildConfigField("String", "YANDEX_MAPKIT_API_KEY", "\"$yandexMapkitKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    fun Packaging.() {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("META-INF/INDEX.LIST")
            excludes.add("META-INF/io.netty.versions.properties")
        }
    }
}

dependencies {

    implementation("androidx.test.ext:junit-ktx:1.3.0")
    dependencies {
        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
        implementation("androidx.activity:activity-compose:1.7.2")
        implementation(platform("androidx.compose:compose-bom:2023.03.00"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.navigation:navigation-compose:2.7.2")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material3:material3")
        implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
        implementation("com.google.firebase:firebase-database-ktx:20.2.2")
        implementation("com.yandex.android:maps.mobile:4.8.0-full")
        implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
        androidTestImplementation("androidx.compose.ui:ui-test-junit4")
        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")
        implementation("com.airbnb.android:lottie-compose:4.2.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
        implementation("androidx.room:room-ktx:2.6.1")
        androidTestImplementation("androidx.room:room-testing:2.6.1")
        implementation("androidx.compose.runtime:runtime-livedata:1.0.5")
        implementation("io.coil-kt:coil-compose:1.3.2")
        implementation("androidx.room:room-runtime:2.6.1")
        annotationProcessor("androidx.room:room-compiler:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")
        implementation("com.google.code.gson:gson:2.8.8")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
        implementation("androidx.compose.runtime:runtime-livedata:1.5.1")
        implementation("androidx.compose.material:material-icons-extended:1.5.1")
        implementation("at.favre.lib:bcrypt:0.10.2")
    }}
