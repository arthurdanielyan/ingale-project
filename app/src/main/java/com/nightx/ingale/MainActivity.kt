package com.nightx.ingale

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.retainedComponent
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.decompose.AppRouterImpl
import com.nightx.ingale.core.decompose.DefaultAppComponentContext
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.impl.root.RootScreen
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val rootComponentFactory by inject<RootComponent.Factory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IngaleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        BottomNavigation()
//                    }
                    val rootComponent = retainedComponent { componentContext ->
                        rootComponentFactory(
                            appComponentContext = object :
                                AppComponentContext by DefaultAppComponentContext(componentContext) {
                                override val appRouter = AppRouterImpl()
                            }
                        )
                    }
                    RootScreen(rootComponent)
                }
            }
        }
    }
}
