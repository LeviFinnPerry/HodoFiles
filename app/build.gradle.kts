plugins {
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.hodofiles"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hodofiles"
        minSdk = 26
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.preference)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    implementation(libs.activity)
    implementation(libs.places)
    implementation(libs.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}
//secrets {
//    // To add your Maps API key to this project:
//    // 1. If the secrets.properties file does not exist, create it in the same folder as the local.properties file.
//    // 2. Add this line, where YOUR_API_KEY is your API key:
//    //        MAPS_API_KEY=YOUR_API_KEY
//    propertiesFileName = "secrets.properties"
//
//    // A properties file containing default secret values. This file can be
//    // checked in version control.
//    defaultPropertiesFileName = "local.defaults.properties"
//
//    // Configure which keys should be ignored by the plugin by providing regular expressions.
//    // "sdk.dir" is ignored by default.
//    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
//    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
//}