package com.nightx.ingale

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.nightx.featureLocal.core.viewState.featureLocalViewStateModule
import com.nightx.ingale.bottomBar.impl.bottomBar.bottomBarControllerModule
import com.nightx.ingale.bottomBar.impl.snackbar.snackbarMessageSenderModule
import com.nightx.ingale.core.audioPlayer.impl.audioPlayerModule
import com.nightx.ingale.core.dataModel.di.mappersModule
import com.nightx.ingale.core.dataModel.di.realmModule
import com.nightx.ingale.core.utils.di.coroutineDispatchersModule
import com.nightx.ingale.featureLocal.navigation.impl.di.localNavigationModule
import com.nightx.ingale.musicbar.musicBarModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class IngaleApplication : Application() {

    companion object {
        const val MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID = "music_player"
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            androidLogger()
            modules(
                bottomBarControllerModule,
                snackbarMessageSenderModule,
                audioPlayerModule,
                mappersModule,
                realmModule,
                coroutineDispatchersModule,
                featureLocalViewStateModule,
                localNavigationModule,
                musicBarModule,
                com.nightx.ingale.resources.strings.module,
                com.nightx.ingale.featureLocal.featureHome.data.dataModule,
                com.nightx.ingale.featureLocal.featureHome.domain.domainModule,
                *com.nightx.ingale.featureLocal.featureHome.presentation.presentationModule.toTypedArray(),
                *com.nightx.ingale.featureLocal.featureSongsSet.presentation.presentationModule.toTypedArray(),
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