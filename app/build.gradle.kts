plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "se.curtrune.lucy"
    compileSdk = 36

    defaultConfig {
        applicationId = "se.curtrune.lucy"
        minSdk = 24
        targetSdk = 34
        versionCode = 20
        versionName = "the immortal story"

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
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation(platform("androidx.compose:compose-bom:2025.11.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-service:2.9.4")
    implementation("androidx.room:room-ktx:2.8.3")
    implementation("androidx.compose.ui:ui-android:1.9.4")
    implementation("androidx.test.espresso:espresso-core:3.7.0")
    implementation("androidx.fragment:fragment-compose:1.8.9")
    implementation("com.google.firebase:firebase-dataconnect:17.1.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3-android:2.10.0-rc01")
    implementation("androidx.compose.runtime:runtime:1.9.4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.11.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.6")
    implementation ("androidx.compose.material:material-icons-extended")
    implementation("com.google.code.gson:gson:2.13.2")

    //ktor
    implementation("io.ktor:ktor-client-android:3.3.2")
    implementation("io.ktor:ktor-client-core:3.3.2")
    implementation("io.ktor:ktor-client-serialization:3.3.2")
    implementation("io.ktor:ktor-client-logging:3.3.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.3.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.2")

    //kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.9.0")

    //navigation 3
    implementation("androidx.navigation3:navigation3-ui:1.0.0-rc01")
    implementation("androidx.navigation3:navigation3-runtime:1.0.0-rc01")
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3")
    //whatever
    //implementation("ch.qos.logback:logback-classic:1.5.19")

    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation ("com.github.skydoves:colorpickerview:2.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.9.4")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.3.1")

    implementation("androidx.annotation:annotation:1.9.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}