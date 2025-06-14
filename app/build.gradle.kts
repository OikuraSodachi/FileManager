import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.todokanai.filemanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.todokanai.filemanager"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
//    implementation("androidx.work:work-runtime-ktx:2.9.1")
    implementation("androidx.databinding:library:3.2.0-alpha11")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    val roomVersion = "2.6.1"
    val glideVersion = "4.13.0"
    val daggerVersion = "2.49"

    implementation(composeBom)

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-common:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")

    implementation("androidx.lifecycle:lifecycle-runtime-compose-android:2.8.4")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")     // enable ConstraintLayout
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.fragment:fragment-ktx:1.7.1")

    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    debugImplementation("androidx.compose.ui:ui-tooling")      // compose preview
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")

    implementation("com.squareup.picasso:picasso:2.8")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("io.coil-kt:coil-compose:2.1.0")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")     // ConstraintLayout for compose
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.github.bumptech.glide:glide:$glideVersion")
    annotationProcessor("com.github.bumptech.glide:compiler:$glideVersion")
    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerVersion")
    implementation("androidx.datastore:datastore-preferences:1.1.0-alpha01")       // enable DataStore
    implementation("net.lingala.zip4j:zip4j:2.6.0")
    implementation("commons-net:commons-net:3.11.1")       /// ftp nas upload (?)

}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        languageVersion = "1.9"
    }
}