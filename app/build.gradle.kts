plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "org.bmstudio.cra2go"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.bmstudio.cra2go"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "0.121"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["appAuthRedirectScheme"] = "org.bmstudio.cra2go"

        android.buildFeatures.buildConfig  = true


    }



    buildTypes {
        debug {
            buildConfigField("String","BASEURLAPI","\"https://api-sandbox.lufthansa.com/\"");
            buildConfigField("String","BUILDVERSION","\"MOCK\"");
            buildConfigField("String","AUTHENDPOINT","\"https://oauth-test.lufthansa.com/lhcrew/oauth/authorize\"");
            buildConfigField("String","TOKENENDPOINT","\"https://oauth-test.lufthansa.com/lhcrew/oauth/token\"")
            buildConfigField("String","CLIENT_ID","\"st53sq4qnbrvbbbtedgqk3dc\"");
            buildConfigField("String","CLIENT_SECRET","\"rpgtUArDew\"");
            buildConfigField("String","SCOPE","\"https://mock.cms.fra.dlh.de/publicCrewApiDev\"");
            buildConfigField("String","RESOURCEURL","\"/v1/flight_operations/crew_services/mock/COMMON_DUTY_EVENTS\"");
        }
        release {

            buildConfigField("String","BUILDVERSION","\"PROD\"");
            buildConfigField("String","BASEURLAPI","\"https://api.lufthansa.com/\"");
            buildConfigField("String","AUTHENDPOINT","\"https://oauth.lufthansa.com/lhcrew/oauth/authorize\"");
            buildConfigField("String","TOKENENDPOINT","\"https://oauth.lufthansa.com/lhcrew/oauth/token\"");
            buildConfigField("String","CLIENT_ID","\"am39f2kvrg8u6gvatfbfkyre\"");
            buildConfigField("String","CLIENT_SECRET","\"CfsJTAKcpv\"");
            buildConfigField("String","SCOPE","\"https://cms.fra.dlh.de/publicCrewApi\"");
            buildConfigField("String","RESOURCEURL","\"/v1/flight_operations/crew_services/COMMON_DUTY_EVENTS\"");

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
        compose = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.gson)

    // Compose dependencies
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.hilt.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    //implementation(libs.androidx.hilt.lifecycle.viewmodel)
    kapt(libs.androidx.hilt.compiler)

    // Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    // Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    //AppAuth
    implementation(libs.appauth)

    //ActivityContract
    implementation(libs.androidx.activity)

    //Javapoet
    implementation(libs.javapoet)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
}


