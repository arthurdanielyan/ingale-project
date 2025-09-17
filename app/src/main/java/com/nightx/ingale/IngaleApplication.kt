package com.nightx.ingale

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.nightx.ingale.core.audioPlayer.impl.PlayerService.Companion.MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID
import com.nightx.ingale.core.audioPlayer.impl.audioPlayerImplModule
import com.nightx.ingale.core.audioPlayer.impl.playerServiceCommunicatorModule
import com.nightx.ingale.core.dataModel.di.mappersModule
import com.nightx.ingale.core.dataModel.di.repositoriesModule
import com.nightx.ingale.core.dataModel.di.roomModule
import com.nightx.ingale.core.domain.di.coreDomainModule
import com.nightx.ingale.core.presentation.osExt.di.osExtModule
import com.nightx.ingale.core.utils.di.coroutineDispatchersModule
import com.nightx.ingale.featureLocal.core.presentation.common.featureLocalCommonUiModule
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.di.createNewPlaylistComponentFactoryModule
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.di.currentPlaylistsBottomSheetComponentFactoryModule
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.di.songOperationsParentComponentFactoryModule
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.di.songOperationsBottomSheetComponentFactoryModule
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.di.songsListComponentFactoryModule
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
                repositoriesModule,
                coroutineDispatchersModule,
                featureLocalCommonUiModule,
                createNewPlaylistComponentFactoryModule,
                songOperationsBottomSheetComponentFactoryModule,
                songOperationsParentComponentFactoryModule,
                songsListComponentFactoryModule,
                currentPlaylistsBottomSheetComponentFactoryModule,
                localComponentFactoryModule,
                requirePermissionsComponentFactoryModule,
                *globalPlaybackPresentationModule.toTypedArray(),
                com.nightx.ingale.resources.strings.module,
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