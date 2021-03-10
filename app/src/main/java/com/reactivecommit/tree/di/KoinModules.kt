package com.reactivecommit.tree.di

import androidx.room.Room
import com.reactivecommit.tree.RootRepository
import com.reactivecommit.tree.data.database.Database
import com.reactivecommit.tree.data.database.RoomDb
import com.reactivecommit.tree.data.database.RootDatabase
import com.reactivecommit.tree.data.network.NetworkService
import org.koin.dsl.module

val dataModule = module {

    single { Room.databaseBuilder(get(), RootDatabase::class.java, "tree-db").build() }

    single {
        RoomDb(
            get<RootDatabase>(RootDatabase::class.java).houseDao(),
            get<RootDatabase>(RootDatabase::class.java).characterDao()
        ) as Database
    }

    single {
        NetworkService.api
    }

    single { RootRepository }

}