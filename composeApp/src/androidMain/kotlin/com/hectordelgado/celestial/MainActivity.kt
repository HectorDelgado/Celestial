package com.hectordelgado.celestial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hectordelgado.celestial.di.appModule
import com.hectordelgado.celestial.di.sharedModule
import com.hectordelgado.celestial.feature.core.app.App
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}
