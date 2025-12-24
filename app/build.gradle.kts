plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.hc.chess"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hc.chess"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "OAUTH_CLIENT_ID", properties["OAUTH_CLIENT_ID"] as String)
        buildConfigField("String", "OAUTH_CLIENT_SECRET", properties["OAUTH_CLIENT_SECRET"] as String)
        buildConfigField("String", "OAUTH_AUTHORIZATION_ENDPOINT", properties["OAUTH_AUTHORIZATION_ENDPOINT"] as String)
        buildConfigField("String", "OAUTH_TOKEN_ENDPOINT", properties["OAUTH_TOKEN_ENDPOINT"] as String)
        buildConfigField("String", "OAUTH_LOGOUT_ENDPOINT", properties["OAUTH_LOGOUT_ENDPOINT"] as String)
        buildConfigField("String", "OAUTH_PLAYER_INFO_ENDPOINT", properties["OAUTH_PLAYER_INFO_ENDPOINT"] as String)
        buildConfigField("String", "OAUTH_REDIRECT_SCHEME", properties["OAUTH_REDIRECT_SCHEME"] as String)
        buildConfigField("String", "OAUTH_REDIRECT_URI", properties["OAUTH_REDIRECT_URI"] as String)
        buildConfigField("String", "OAUTH_LOGOUT_REDIRECT_URI", properties["OAUTH_LOGOUT_REDIRECT_URI"] as String)
        buildConfigField("String", "OAUTH_GRANT_TYPE", properties["OAUTH_GRANT_TYPE"] as String)
        buildConfigField("String", "OAUTH_GRANT_TYPE_PARAMETER_NAME", properties["OAUTH_GRANT_TYPE_PARAMETER_NAME"] as String)

        buildConfigField("String", "SIGN_UP_CLIENT_ID", properties["SIGN_UP_CLIENT_ID"] as String)
        buildConfigField("String", "SIGN_UP_CLIENT_SECRET", properties["SIGN_UP_CLIENT_SECRET"] as String)
        buildConfigField("String", "SIGN_UP_TOKEN_ENDPOINT", properties["SIGN_UP_TOKEN_ENDPOINT"] as String)
        buildConfigField("String", "SIGN_UP_ENDPOINT", properties["SIGN_UP_ENDPOINT"] as String)
        buildConfigField("String", "SIGN_UP_O_AUTH_TYPE_KEY", properties["SIGN_UP_O_AUTH_TYPE_KEY"] as String)
        buildConfigField("String", "SIGN_UP_O_AUTH_TYPE_VALUE", properties["SIGN_UP_O_AUTH_TYPE_VALUE"] as String)
        buildConfigField("String", "PIECE_MOVEMENT_ENDPOINT", properties["PIECE_MOVEMENT_ENDPOINT"] as String)
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.browser)
    implementation(libs.okhttp)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}