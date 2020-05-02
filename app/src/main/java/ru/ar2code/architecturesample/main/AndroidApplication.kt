package ru.ar2code.architecturesample.main

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Приложение - это набор наших реализаций, которые мы подключаем.
 * Я использую DI Koin для внедрения зависимостей.
 */
class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AndroidApplication)

            modules(
                listOf(appModule)
            )
        }
    }
}