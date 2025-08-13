package com.nightx.ingale

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import com.arkivanov.decompose.retainedComponent
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.decompose.AppRouterImpl
import com.nightx.ingale.core.decompose.DefaultAppComponentContext
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.impl.root.ui.RootScreen
import org.koin.android.ext.android.inject

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
            window.navigationBarColor = MaterialTheme.colorScheme.surface.toArgb()
            IngaleTheme {

                enableEdgeToEdge(
                    navigationBarStyle = SystemBarStyle.auto(
                        MaterialTheme.colorScheme.surface.toArgb(),
                        MaterialTheme.colorScheme.surface.toArgb(),
                    )
                )
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootScreen(rootComponent)
                }
            }
        }
    }

    @Composable
    private fun StatusBarProtection(
        color: Color = MaterialTheme.colorScheme.surfaceContainer,
        heightProvider: () -> Float = calculateGradientHeight(),
    ) {

        Canvas(Modifier.fillMaxSize()) {
            val calculatedHeight = heightProvider()
            val gradient = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 1f),
                    color.copy(alpha = .8f),
                    Color.Transparent
                ),
                startY = 0f,
                endY = calculatedHeight
            )
            drawRect(
                brush = gradient,
                size = Size(size.width, calculatedHeight),
            )
        }
    }

    @Composable
    fun calculateGradientHeight(): () -> Float {
        val statusBars = WindowInsets.statusBars
        val density = LocalDensity.current
        return { statusBars.getTop(density).times(1.2f) }
    }
}
