plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // ESTE PLUGIN ES VITAL PARA QUE LEA EL JSON
    id("com.google.gms.google-services")
}

android {
    namespace = "com.miraflores.agenda"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.miraflores.agenda"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    packaging { jniLibs { useLegacyPackaging = true } }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }
    buildFeatures { viewBinding = true }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // FIREBASE y VOLLEY (Para notificaciones)
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.android.volley:volley:1.2.1")

    // MAPAS
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
}