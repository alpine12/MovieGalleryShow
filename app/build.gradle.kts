plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id ("com.google.devtools.ksp")
    id ("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.alpine12.moviegalleryshow"
    compileSdk = 35
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.alpine12.moviegaleryshow"
        minSdk = 21
        targetSdk = 34
        versionCode = 4
        versionName = "1.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val movieDbApiKey = project.property("moviedb_access_key") as String
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "baseUrl", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "imageUrl", "\"https://image.tmdb.org/t/p/original/\"")
            buildConfigField("String", "apiKey", movieDbApiKey)
            buildConfigField("String", "youtubeUrl", "\"https://img.youtube.com/vi/\"")
        }
        debug {
            buildConfigField("String", "baseUrl", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "imageUrl", "\"https://image.tmdb.org/t/p/original/\"")
            buildConfigField("String", "apiKey", movieDbApiKey)
            buildConfigField("String", "youtubeUrl", "\"https://img.youtube.com/vi/\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    val fragmentKtxVersion = "1.8.8"
    val navVersion = "2.9.3"
    val lifecycleVersion = "2.9.2"
    val lottieVersion = "3.7.0"
    val timberVersion = "4.7.1"
    val retrofitVersion = "2.9.0"
    val okhttpVersion = "4.9.1"
    val hiltVersion = "2.48"
    val hiltViewModelVersion = "1.2.0"
    val keyboardVisibilityVersion = "3.0.0-RC3"
    val pagingVersion = "3.3.6"
    val glideVersion = "4.16.0"
    val shimmerVersion = "0.5.0"
    val roomVersion = "2.7.2"

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.fragment:fragment-ktx:$fragmentKtxVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("com.airbnb.android:lottie:$lottieVersion")
    implementation("com.jakewharton.timber:timber:$timberVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation ("com.google.dagger:hilt-android:2.56.2")
    ksp ("com.google.dagger:hilt-compiler:2.56.2")
    implementation("net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:$keyboardVisibilityVersion")
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.room:room-paging:$roomVersion")
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.facebook.shimmer:shimmer:$shimmerVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}

kapt {
    correctErrorTypes = true
}
