package com.hectordelgado.celestial

import android.app.Application
import com.hectordelgado.celestial.di.appModule
import com.hectordelgado.celestial.di.initKoin
import com.hectordelgado.celestial.di.platformModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MainApp)
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