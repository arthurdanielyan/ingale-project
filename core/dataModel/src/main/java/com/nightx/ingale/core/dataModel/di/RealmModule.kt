package com.nightx.ingale.core.dataModel.di

import com.nightx.ingale.core.dataModel.SongRealm
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