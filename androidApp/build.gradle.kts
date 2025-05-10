plugins {
    id("com.android.application")
    id("at.asitplus.gradle.conventions")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

val apkSignerPassword =
    (findProperty("android.cert.password") as String?) ?: System.getenv("ANDROID_CERT_PASSWORD")

//tasks.withType<com.android.build.gradle.internal.tasks.DexArchiveBuilderTask>().configureEach {
//    doFirst {
//        println("Sorting inputs for DEX task: ${name}")
//        inputs.files.files.sorted().forEach {
//            println(" -> ${it.relativeTo(project.projectDir)}")
//        }
//    }
//}

//tasks.withType<AbstractArchiveTask>().configureEach {
//    isPreserveFileTimestamps = false
//    isReproducibleFileOrder = true
//}



android {
    namespace = "at.asitplus.wallet.app.android"

    signingConfigs {
        getByName("debug") {
            storeFile = file("keystore.p12")
            storePassword = apkSignerPassword
            keyAlias = "key0"
            keyPassword = apkSignerPassword
        }
        create("release") {
            storeFile = file("keystore.p12")
            storePassword = apkSignerPassword
            keyAlias = "key0"
            keyPassword = apkSignerPassword
        }
    }
    defaultConfig {
        applicationId = "at.asitplus.wallet.app.android"
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = (findProperty("version.code") as String).toInt()
        versionName = findProperty("version.name") as String
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles((getDefaultProguardFile("proguard-android-optimize.txt")))
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildTypes.configureEach {
        isCrunchPngs = false
    }

    packaging {
        jniLibs.useLegacyPackaging = true
        resources.excludes += ("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
        resources.excludes += ("META-INF/AL2.0")
        resources.excludes += ("META-INF/LGPL2.1")
        resources.excludes += ("META-INF/LICENSE.md")
    }
    // post_permissions for mulitpaz
    lint {
        baseline = file("lint-baseline.xml")
    }
}
dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.registry.provider)
    implementation(libs.koin.core)
    implementation(libs.multipaz)
    implementation(libs.datastore.preferences.core)
    implementation(libs.core.splashscreen)
}
