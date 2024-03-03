package com.nightx.ingale

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.nightx.ingale.core.coreModule
import com.nightx.ingale.feature_local.featureLocalModule
import com.nightx.ingale.feature_yt.ytModule
import com.nightx.ingale.main_navigation.bottom_bar_controls.bottomBarControllerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class IngaleApplication : Application() {

    companion object {
        const val MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID = "music_player"
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                *coreModule.toTypedArray(),
                bottomBarControllerModule,
                *featureLocalModule.toTypedArray(),
                ytModule
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val musicNotificationChannel = NotificationChannel(
                MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(musicNotificationChannel)
        }
    }
}