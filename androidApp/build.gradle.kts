plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("at.asitplus.gradle.conventions")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("org.gradlex.reproducible-builds") version "1.0"
}

kotlin {
    androidTarget()
    sourceSets {
        androidMain.dependencies {
            implementation(project(":shared"))
            implementation(libs.play.services.identity.credentials)

            implementation(libs.multipaz)
            implementation(libs.multipaz.android.legacy)
        }
    }
}

//val apkSignerPassword =
//    (findProperty("android.cert.password") as String?) ?: System.getenv("ANDROID_CERT_PASSWORD")
val apkSignerPassword = "testtest"

tasks.withType<Copy>().configureEach {
    filter { line -> line.replace("\r\n", "\n").trimEnd() }
}

//tasks.withType<com.android.build.gradle.internal.tasks.DexArchiveBuilderTask>().configureEach {
//    doFirst {
//        println("Sorting inputs for DEX task: ${name}")
//        inputs.files.files.sorted().forEach {
//            println(" -> ${it.relativeTo(project.projectDir)}")
//        }
//    }
//}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

android {
    namespace = "at.asitplus.wallet.app.android"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

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
//            isMinifyEnabled = true    // this makes the two builds the same (tested in docker)
//            proguardFiles((getDefaultProguardFile("proguard-android-optimize.txt")))
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildTypes.configureEach {
        isCrunchPngs = false
    }

    packaging {
        jniLibs.useLegacyPackaging = true
        resources.excludes += ("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
    }
}
dependencies {
    implementation(libs.core.splashscreen)
}

repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}