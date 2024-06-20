plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.lokatravel"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.lokatravel"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


        buildConfigField ("String", "FIRST_BASE_URL", "\"https://authentication-api-k4qtulmtyq-et.a.run.app/\"")
        buildConfigField ("String", "SECOND_BASE_URL", "\"https://newsapi.org/v2/\"")
        buildConfigField ("String", "THIRD_BASE_URL", "\"https://tourism-data-api-k4qtulmtyq-et.a.run.app/places/\"")
        buildConfigField("String", "API_KEY", "\"e90b9f43849c46338aa2d4c38845d8fa\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField ("String", "FIRST_BASE_URL", "\"https://authentication-api-k4qtulmtyq-et.a.run.app/\"")
            buildConfigField ("String", "SECOND_BASE_URL", "\"https://newsapi.org/v2/\"")
            buildConfigField ("String", "THIRD_BASE_URL", "\"https://tourism-data-api-k4qtulmtyq-et.a.run.app/places/\"")
        }
        release {
            buildConfigField ("String", "FIRST_BASE_URL", "\"https://authentication-api-k4qtulmtyq-et.a.run.app/\"")
            buildConfigField ("String", "SECOND_BASE_URL", "\"https://newsapi.org/v2/\"")
            buildConfigField ("String", "THIRD_BASE_URL", "\"https://tourism-data-api-k4qtulmtyq-et.a.run.app/places/\"")
            buildConfigField("String", "API_KEY", "\"e90b9f43849c46338aa2d4c38845d8fa\"")

            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        dataBinding = true
        mlModelBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.espresso.idling.resource)
    implementation(libs.androidx.room.ktx)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.core)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.tensorflow.lite.gpu)
    implementation(libs.play.services.fitness)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation ("androidx.datastore:datastore-preferences:1.1.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")


    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    // Maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")

    implementation ("org.tensorflow:tensorflow-lite:2.8.0")
    implementation ("org.tensorflow:tensorflow-lite-support:0.2.0")
}