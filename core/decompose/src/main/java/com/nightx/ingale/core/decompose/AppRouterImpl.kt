package com.nightx.ingale.core.decompose

class AppRouterImpl(
    private val onNavigate: ((ScreenConfig, onComplete: (Boolean) -> Unit) -> Unit)? = null,
    private val onPop: (((Boolean) -> Unit) -> Unit)? = null,
    private val parentRouter: AppRouter? = null,
) : AppRouter {

    override fun navigate(dest: ScreenConfig) {
        onNavigate?.invoke(dest) { isSuccess ->
            if (isSuccess.not()) {
                parentRouter?.navigate(dest)
            }
        } ?: parentRouter?.navigate(dest)
    }

    override fun pop() {
        onPop?.invoke { isSuccess ->
            if (isSuccess.not()) {
                parentRouter?.pop()
            }
        } ?: parentRouter?.pop()
    }
}