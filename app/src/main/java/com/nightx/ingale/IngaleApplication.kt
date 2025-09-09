package com.nightx.ingale

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.nightx.ingale.core.audioPlayer.impl.PlayerService.Companion.MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID
import com.nightx.ingale.core.audioPlayer.impl.audioPlayerImplModule
import com.nightx.ingale.core.audioPlayer.impl.playerServiceCommunicatorModule
import com.nightx.ingale.core.dataModel.di.coreRepositoriesModule
import com.nightx.ingale.core.dataModel.di.mappersModule
import com.nightx.ingale.core.dataModel.di.roomModule
import com.nightx.ingale.core.domainModel.di.coreDomainModule
import com.nightx.ingale.core.presentation.osExt.di.osExtModule
import com.nightx.ingale.core.utils.di.coroutineDispatchersModule
import com.nightx.ingale.featureLocal.core.ui.featureLocalCoreUiModule
import com.nightx.ingale.featureLocal.featureHome.presentation.di.featureHomePresentationModule
import com.nightx.ingale.featureLocal.featureRequirePermissions.di.requirePermissionsComponentFactoryModule
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.di.songsSetPresentationModule
import com.nightx.ingale.featureLocal.navigation.impl.di.localComponentFactoryModule
import com.nightx.ingale.globalPlaybackPresentation.di.globalPlaybackPresentationModule
import com.nightx.ingale.root.impl.di.rootModule
import com.nightx.ingale.root.impl.snackbar.snackbarMessageSenderModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class IngaleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            androidLogger()
            modules(
                osExtModule,
                audioPlayerImplModule,
                rootModule,
                snackbarMessageSenderModule,
                playerServiceCommunicatorModule,
                mappersModule,
                roomModule,
                coreDomainModule,
                coreRepositoriesModule,
                coroutineDispatchersModule,
                featureLocalCoreUiModule,
                localComponentFactoryModule,
                requirePermissionsComponentFactoryModule,
                *globalPlaybackPresentationModule.toTypedArray(),
                com.nightx.ingale.resources.strings.module,
                com.nightx.ingale.featureLocal.featureHome.data.dataModule,
                com.nightx.ingale.featureLocal.featureHome.domain.domainModule,
                *featureHomePresentationModule.toTypedArray(),
                *songsSetPresentationModule.toTypedArray(),
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val musicNotificationChannel = NotificationChannel(
                MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(musicNotificationChannel)
        }
    }
}