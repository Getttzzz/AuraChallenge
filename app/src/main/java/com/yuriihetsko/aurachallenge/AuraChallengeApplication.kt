package com.yuriihetsko.aurachallenge

import android.app.Application
import com.yuriihetsko.aurachallenge.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AuraChallengeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AuraChallengeApplication)
            modules(appComponent)
        }
    }
}