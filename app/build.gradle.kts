plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.cinemamanager"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.cinema"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    configurations.all {
        resolutionStrategy {
            // Loại bỏ các phiên bản cũ của annotations
            exclude(group = "org.jetbrains", module = "annotations")
            // Hoặc force một version cụ thể
            force("org.jetbrains:annotations:23.0.0")
        }
    }
}

dependencies {
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.nex3z:flow-layout:1.3.3")
    implementation("com.wefika:flowlayout:0.4.1")
    // AndroidX & UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.2")

    // Flow Layout
    implementation("com.nex3z:flow-layout:1.3.3")

    // ZXing (QR Code)
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // Circle ImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("org.jetbrains:annotations:23.0.0")

    implementation("com.afollestad.material-dialogs:core:0.9.6.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-auth")

}