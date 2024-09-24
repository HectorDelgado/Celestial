package com.hectordelgado.celestial

import android.app.Application
import com.hectordelgado.celestial.di.appModule
import com.hectordelgado.celestial.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            //androidContext()
            androidContext(this@MainApp)
            modules(sharedModule())
            modules(appModule())
        }

        //SoLoader.init(this, false)

//        if (BuildConfig.DEBUG) {
//            val client = AndroidFlipperClient.getInstance(this)
//            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
//            val networkFlipperPlugin = NetworkFlipperPlugin()
//            client.addPlugin(networkFlipperPlugin)
//            client.start()
//        }
    }
}