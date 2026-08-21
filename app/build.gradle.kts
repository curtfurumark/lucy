import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin.Companion.kotlinNodeJsEnvSpec

plugins {
    id("com.android.application")
    //id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
}

android {
    namespace = "se.curtrune.lucy"
    compileSdk = 37

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
    /*
    kotlinOptions {
        jvmTarget = "11"
    }*/
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
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.11.0")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.core:core-ktx:1.19.0")
    implementation(platform("androidx.compose:compose-bom:2026.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.5.0-alpha26")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-service:2.11.0")
    implementation("androidx.room:room-ktx:2.8.4")
    implementation("androidx.compose.ui:ui-android:1.12.0")
    implementation("androidx.test.espresso:espresso-core:3.7.0")
    implementation("androidx.fragment:fragment-compose:1.9.0")
    implementation("com.google.firebase:firebase-dataconnect:17.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3-android:2.11.0")
    implementation("androidx.compose.runtime:runtime:1.12.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2026.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    implementation("androidx.appcompat:appcompat:1.8.0")
    implementation("com.google.android.material:material:1.14.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.8")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.8")
    implementation ("androidx.compose.material:material-icons-extended")
    implementation("com.google.code.gson:gson:2.14.0")
    //implementation("org.jetbrains.kotlin.plugin.compose:2.1.0")

    //ktor
    implementation("io.ktor:ktor-client-android:3.5.2")
    implementation("io.ktor:ktor-client-core:3.5.2")
    implementation("io.ktor:ktor-client-serialization:3.5.2")
    implementation("io.ktor:ktor-client-logging:3.5.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.5.2")
    //install json
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.2")

    //kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.11.0")

    //room
    implementation("androidx.room:room-runtime:2.8.4")
    //implementation("androidx.room:room-ktx:2.8.4")
    ksp ("androidx.room:room-compiler:2.8.4")



    //navigation 3
    implementation("androidx.navigation3:navigation3-ui:1.1.6")
    implementation("androidx.navigation3:navigation3-runtime:1.1.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3")
    //whatever
    //implementation("ch.qos.logback:logback-classic:1.5.19")

    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation ("com.github.skydoves:colorpickerview:2.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.11.0")

    implementation("androidx.annotation:annotation:1.10.0")
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}