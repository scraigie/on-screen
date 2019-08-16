package uk.co.scraigie.onscreen

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import uk.co.scraigie.onscreen.di.dependencyModules


class OnScreenApplication : Application() {

    override fun onCreate(){
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        startKoin {
            androidContext(this@OnScreenApplication)
            modules(dependencyModules)
        }
    }
}