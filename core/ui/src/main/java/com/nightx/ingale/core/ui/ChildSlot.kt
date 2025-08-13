package com.nightx.ingale.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.yield

//@Composable
//fun <C : Any, T : Any> ChildSlot(
//    childSlot: Value<ChildSlot<C, T>>,
//    content: @Composable (Child<C, T>) -> Unit
//) {
//    val child: ChildSlot<C, T> by childSlot.subscribeAsState()
//    content(child)
//}

//@Composable
//fun <C : Any, T : Any> ChildrenSlot(
//    slot: Value<ChildSlot<C, T>>,
//    content: @Composable (Child.Created<C, T>) -> Unit
//) {
//    val childSlot by slot.subscribeAsState()
//    val child = childSlot.child
//    if (child is Child.Created) {
//        content(childSlot.child as Child.Created<C, T>)
//    }
//}

@Composable
fun <C : Any, T : Any> AnimatedChildSlot(
    slot: Value<ChildSlot<C, T>>,
    modifier: Modifier = Modifier,
    enterAnimation: EnterTransition = fadeIn() + expandIn(),
    exitAnimation: ExitTransition = shrinkOut() + fadeOut(),
    content: @Composable (Child.Created<C, T>) -> Unit
) {
    val childSlot by slot.subscribeAsState()
    var visibleChild by remember {
        mutableStateOf(
            childSlot.child
        )
    }
    var isVisible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(childSlot.child) {
        if (it != null) {
            visibleChild = it
            yield()
            isVisible = true
        } else {
            isVisible = false
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = enterAnimation,
        exit = exitAnimation
    ) {

        if (visibleChild != null) {
            content(visibleChild as Child.Created<C, T>)
        }

        DisposableEffect(Unit) {
            onDispose {
                visibleChild = null
            }
        }
    }
}
