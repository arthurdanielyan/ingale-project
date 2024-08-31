package com.nightx.ingale.resources.strings

import org.koin.dsl.module

val module = module {
    single<StringProvider> {
        StringProviderImpl(get())
    }
}