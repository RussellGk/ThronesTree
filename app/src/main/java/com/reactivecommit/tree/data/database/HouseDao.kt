package com.reactivecommit.tree.data.database

import androidx.room.*

@Dao
abstract class HouseDao :
        BaseDao<House> {

    @Query("SELECT * FROM House")
    abstract fun getAll(): List<House>

    fun removeAll() {
        removeAllHouses()
        removeAllRelatives()
    }

    @Query("DELETE FROM House")
    abstract fun removeAllHouses()

    @Query("DELETE FROM Relative")
    abstract fun removeAllRelatives()

    @Query("SELECT count(*) FROM House")
    abstract fun getCount(): Int

    @Insert
    abstract fun insertRelative(vararg relative: Relative)
}