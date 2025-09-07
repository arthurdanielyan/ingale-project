package com.nightx.ingale.core.audioPlayer.impl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.nightx.ingale.core.utils.launchSingle
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PlayerServiceConnection(
    private val applicationContext: Context,
    private val applicationScope: CoroutineScope,
) {

    private var _playerServiceActions: PlayerServiceActions? = null
    private var serviceConnection: ServiceConnection? = null

    private val playerServiceActions: PlayerServiceActions
        get() = _playerServiceActions ?: throw IllegalStateException("Service not bound")

    fun withConnection(
        block: PlayerServiceActions.() -> Unit
    ) {
        applicationScope.launchSingle("player_service_event") {
            bindIfUnbound()
            playerServiceActions.block()
        }
    }

    private suspend fun bindIfUnbound() {
        suspendCoroutine { continuation ->
            if (serviceConnection == null) {
                val intent = Intent(applicationContext, PlayerService::class.java)

                val connection = getConnection(continuation)
                serviceConnection = connection

                applicationContext.bindService(
                    intent,
                    connection,
                    Context.BIND_AUTO_CREATE
                )
            } else {
                continuation.resume(Unit)
            }
        }
    }

    fun unbind() {
        serviceConnection?.let {
            applicationContext.unbindService(it)
            serviceConnection = null
            _playerServiceActions = null
        }
    }

    private fun getConnection(
        continuation: Continuation<Unit>
    ) = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            (service as? PlayerServiceActions)?.let {
                _playerServiceActions = it
            }
            continuation.resume(Unit)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            _playerServiceActions = null
            serviceConnection = null
            continuation.resumeWithException(RuntimeException("I genuinely don't know how this could have happened"))
        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
            _playerServiceActions = null
            serviceConnection = null
            continuation.resumeWithException(RuntimeException("I genuinely don't know how this could have happened"))
        }
    }
}