package com.nightx.ingale

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.nightx.ingale.core.coreModule
import com.nightx.ingale.feature_local.featureLocalModule
import com.nightx.ingale.feature_yt.ytModule
import com.nightx.ingale.main_navigation.bottomBarControls.bottomBarControllerModule
import com.nightx.ingale.main_navigation.musicBar.musicBarModule
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
                musicBarModule,
                *featureLocalModule.toTypedArray(),
                ytModule
            )
            modules(
                com.nightx.ingale.bottomBar.impl.bottomBarControllerModule,
                com.nightx.ingale.core.audioPlayer.impl.audioPlayerModule,
                com.nightx.ingale.core.dataModel.di.mappersModule,
                com.nightx.ingale.core.dataModel.di.realmModule,
                com.nightx.ingale.core.utils.di.coroutinesModule,
                com.nightx.featureLocal.core.viewState.featureLocalViewStateModule,
                com.nightx.ingale.featureLocal.featureHome.data.dataModule,
                com.nightx.ingale.featureLocal.featureHome.domain.domainModule,
                *com.nightx.ingale.featureLocal.featureHome.presentation.presentationModule.toTypedArray(),

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