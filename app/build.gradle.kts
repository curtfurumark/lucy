plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "se.curtrune.lucy"
    compileSdk = 35

    defaultConfig {
        applicationId = "se.curtrune.lucy"
        minSdk = 24
        targetSdk = 34
        versionCode = 18
        versionName = "creature of doom"

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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.core:core-ktx:1.16.0")
    implementation(platform("androidx.compose:compose-bom:2025.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation(platform("androidx.compose:compose-bom:2025.04.01"))
    implementation(platform("androidx.compose:compose-bom:2025.04.01"))
    implementation(platform("androidx.compose:compose-bom:2025.04.01"))
    implementation(platform("androidx.compose:compose-bom:2025.04.01"))
    implementation(platform("androidx.compose:compose-bom:2025.04.01"))
    implementation(platform("androidx.compose:compose-bom:2025.04.01"))
    implementation("androidx.lifecycle:lifecycle-service:2.8.7")
    implementation("androidx.room:room-ktx:2.7.1")
    implementation("androidx.compose.ui:ui-android:1.8.0")
    implementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.04.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.04.01"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.04.01"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.04.01"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.04.01"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.04.01"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.04.01"))
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")
    implementation ("androidx.compose.material:material-icons-extended")
    implementation("com.google.code.gson:gson:2.11.0")

    //ktor
    implementation("io.ktor:ktor-client-android:1.6.3")
    implementation("io.ktor:ktor-client-core:1.6.3")
    implementation("io.ktor:ktor-client-serialization:1.6.3")
    implementation("io.ktor:ktor-client-logging:1.6.3")
    //kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    //whatever
    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation ("com.github.skydoves:colorpickerview:2.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.8.7")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:3.9.0")

    implementation("androidx.annotation:annotation:1.9.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}