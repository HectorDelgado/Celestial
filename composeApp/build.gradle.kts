import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.hectordelgado.celestial.SecretsExtractor
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.buildKonfig.gradlePlugin)
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)

    kotlin(libs.plugins.kotlinSerialization.get().pluginId)
        .version(libs.versions.kotlinSerializationPlugin.get())

    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.sqldelight)
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
            implementation(libs.cashapp.sqldelight.androidDriver)
        }
        commonMain.dependencies {
            // Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // AndroidX
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Coil3
            implementation(libs.coil3.core)
            implementation(libs.coil3.compose)
            implementation(libs.coil3.composeCore)
            implementation(libs.coil3.ktor)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.test)

            // Kotlinx
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            // Ktor
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.json)

            // Okio
            implementation("com.squareup.okio:okio:3.9.1")

            // SqlDelight
            implementation(libs.cashapp.sqldelight.runtime)

            // Voyager
            implementation(libs.voyager.koin)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenModel)

            // todo: see if this is still needed
            implementation("co.touchlab:stately-common:2.0.5")

            //api("org.lighthousegames:logging:1.5.0")

        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.cashapp.sqldelight.nativeDriver)

            // todo: see if this is still needed
            implementation("co.touchlab:stately-isolate:2.0.5")
            implementation("co.touchlab:stately-iso-collections:2.0.5")
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
            isMinifyEnabled = true
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

        // Fliipper
        //debugImplementation(libs.flipper)
        //debugImplementation(libs.flipper.noop)
        //releaseImplementation(libs.soloader)

        // Koin
        implementation(libs.koin.android)


    }
}

buildkonfig {
    packageName = "com.hectordelgado.celestial"

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
