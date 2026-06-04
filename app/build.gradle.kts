plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleServices)
}

android {
    namespace = "com.softage.net.vilcorp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.softage.net.vilcorp"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    android {
        buildFeatures {
            viewBinding = true
            buildConfig = true
        }
    }
    buildFeatures {
        viewBinding= true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.firebase.messaging)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // Activity KTX (for by viewModels delegate)
    implementation("androidx.activity:activity-ktx:1.9.3")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

    // Coroutines Android
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    // Mukesh OTP View
    implementation("com.github.rajputmukesh748:MukeshOtpView:1.0.0")



    // Google Play Services Auth (Auto Read OTP)
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.android.gms:play-services-auth-api-phone:18.1.0")

    //progress bar for performance
    implementation("com.github.antonKozyriatskyi:CircularProgressIndicator:1.3.0")


    implementation ("com.github.gcacace:signature-pad:1.3.1")
    //retrofit
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.google.code.gson:gson:2.8.9")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    implementation("com.google.android.material:material:1.3.0-alpha02")
    implementation("androidx.biometric:biometric:1.2.0-alpha04") // Latest version as of now
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.github.Marvel999:Android-Loading-Animation:1.0.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    implementation ("androidx.camera:camera-camera2:1.3.3")
    implementation ("androidx.camera:camera-lifecycle:1.3.3")
    implementation ("androidx.camera:camera-view:1.3.3")
}