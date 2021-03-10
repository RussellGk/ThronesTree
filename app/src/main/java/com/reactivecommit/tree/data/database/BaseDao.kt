package com.reactivecommit.tree.data.database

import androidx.room.*

interface BaseDao<T> {
    @Insert
    fun insert(obj: T)

    @Insert
    fun insert(vararg obj: T)

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)
}