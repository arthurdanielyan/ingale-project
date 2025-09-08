package com.nightx.ingale.core.dataModel.di

import androidx.room.Room
import com.nightx.ingale.core.dataModel.room.AppDatabase
import com.nightx.ingale.core.dataModel.room.songsCache.dao.SongsCacheDao
import org.koin.dsl.module

val roomModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "app.db"
        ).build()
    }

    single<SongsCacheDao> {
        get<AppDatabase>().songsCacheDao
    }
}