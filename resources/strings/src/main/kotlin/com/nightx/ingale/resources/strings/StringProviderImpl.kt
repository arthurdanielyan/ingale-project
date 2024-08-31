package com.nightx.ingale.resources.strings

import android.content.Context

internal class StringProviderImpl(
    private val applicationContext: Context,
) : StringProvider {

    override fun string(resId: Int, vararg args: Any): String {
        return applicationContext.getString(resId, args)
    }
}