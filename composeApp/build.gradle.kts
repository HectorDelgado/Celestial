import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.hectordelgado.celestial.SecretsExtractor
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget



buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.15.2")
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "1.9.20"
    id("com.codingfeline.buildkonfig") version "0.15.2"
    id("app.cash.sqldelight") version "2.0.2"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation("app.cash.sqldelight:android-driver:2.0.2")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            //implementation("io.coil-kt:coil:2.7.0")

            implementation("io.coil-kt.coil3:coil:3.0.0-alpha10")
            implementation("io.coil-kt.coil3:coil-compose:3.0.0-alpha10")
            implementation("io.coil-kt.coil3:coil-compose-core:3.0.0-alpha10")
            implementation("io.coil-kt.coil3:coil-network-ktor:3.0.0-alpha08")

            //implementation("io.coil-kt.coil3:coil-network-okhttp:2.7.0")

            val voyagerVersion = "1.1.0-beta02"

            // Multiplatform

            // Navigator
            implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")

            // Screen Model
            implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")

            implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion")

            val koin = "3.2.0"
            implementation("io.insert-koin:koin-core:$koin")
            implementation("io.insert-koin:koin-test:$koin")

            api("org.lighthousegames:logging:1.4.2")
            implementation("co.touchlab:stately-common:2.0.5")
            implementation("app.cash.sqldelight:runtime:2.0.2")
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation("co.touchlab:stately-isolate:2.0.5")
            implementation("co.touchlab:stately-iso-collections:2.0.5")
            implementation("app.cash.sqldelight:native-driver:2.0.2")
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.hectordelgado.celestial"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.hectordelgado.celestial"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
        implementation(libs.kotlinx.coroutines.android)

        debugImplementation("com.facebook.flipper:flipper:0.266.0")
        debugImplementation("com.facebook.soloader:soloader:0.10.5")
        debugImplementation("com.facebook.flipper:flipper-network-plugin:0.266.0")
        releaseImplementation("com.facebook.flipper:flipper-noop:0.266.0")
        implementation("com.facebook.soloader:soloader:0.12.1+")

        implementation("io.insert-koin:koin-android:4.0.0")
    }
}

buildkonfig {
    packageName = "com.hectordelgado.celestial"
    // objectName = "YourAwesomeConfig"
    // exposeObjectWithName = "YourAwesomePublicConfig"

    val extractor = SecretsExtractor()
    val secrets = extractor.loadSecrets("$rootDir/secret.properties")

    defaultConfigs {
        buildConfigField(STRING, "NASA_API_KEY", secrets.getProperty("NASA_API_KEY"))
    }
}

sqldelight {
    databases {
        create("SqlDelightDatabase") {
            packageName.set("com.hectordelgado.celestial")
        }
    }
}
