plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.certbridge.probe"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.certbridge.probe"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "0.1"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
