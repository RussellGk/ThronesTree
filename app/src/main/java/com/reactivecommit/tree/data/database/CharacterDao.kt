package com.reactivecommit.tree.data.database

import androidx.room.*

@Dao
interface CharacterDao :
        BaseDao<Character> {

    @Query("DELETE FROM Character")
    fun removeAll()

    @Query("""SELECT c.*, h.name as house, h.words
        FROM Character as c
          INNER JOIN Relative AS r ON c.id == r.id
          INNER JOIN House AS h ON h.id == r.house
        WHERE c.id = :id""")
    fun getById(id: String): CharacterWithHouse?

    @Query("""SELECT c.*, h.name as house, h.words
        FROM Character as c
          INNER JOIN Relative AS r ON c.id == r.id
          INNER JOIN House AS h ON h.id == r.house
        WHERE h.name = :houseName""")
    fun getByHouseName(houseName: String): List<CharacterWithHouse>


}