package com.nightx.ingale

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.arkivanov.decompose.retainedComponent
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.decompose.AppRouterImpl
import com.nightx.ingale.core.decompose.DefaultAppComponentContext
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.impl.root.ui.RootScreen
import org.koin.android.ext.android.inject
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {

    private val rootComponentFactory by inject<RootComponent.Factory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootComponent = retainedComponent { componentContext ->
            rootComponentFactory(
                appComponentContext = object :
                    AppComponentContext by DefaultAppComponentContext(componentContext) {
                    override val appRouter = AppRouterImpl()
                }
            )
        }
        setContent {
            IngaleTheme {

                enableEdgeToEdge(
                    navigationBarStyle = SystemBarStyle.auto(
                        MaterialTheme.colorScheme.surface.toArgb(),
                        MaterialTheme.colorScheme.surface.toArgb(),
                    )
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        RootScreen(rootComponent)
                        NavigationBarOverlay()
                    }
                }
            }
        }
    }

    @Composable
    private fun BoxScope.NavigationBarOverlay() {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .requiredHeight(
                    getNavigationBarHeight()
                )
                .background(MaterialTheme.colorScheme.background)
        )
    }

    @Composable
    private fun getNavigationBarHeight(): Dp {
        val navigationBars = WindowInsets.navigationBars
        return LocalDensity.current.run {
            (navigationBars.getTop(this) - navigationBars.getBottom(this))
                .absoluteValue.toDp()
        }
    }
}
