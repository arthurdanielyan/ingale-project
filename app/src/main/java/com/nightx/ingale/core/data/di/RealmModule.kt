package com.nightx.ingale.core.data.di

import com.nightx.ingale.core.data.model.SongRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

val realmModule = module {
    single {
        Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    SongRealm::class
                )
            )
        )
    }
}