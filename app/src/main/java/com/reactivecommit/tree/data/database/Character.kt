package com.reactivecommit.tree.data.database

import androidx.room.*

@Entity
data class Character(
        @PrimaryKey val id: String,
        val name: String,
        val born: String,
        val died: String,
        val titles: List<String>,
        val aliases: List<String>,
        val father: String, //rel
        val mother: String //rel
)

data class CharacterWithHouse(
        val id: String,
        val name: String,
        val words: String,
        val born: String,
        val died: String,
        val titles: List<String>,
        val aliases: List<String>,
        val father: String, //rel
        val mother: String, //rel
        val house: String
)