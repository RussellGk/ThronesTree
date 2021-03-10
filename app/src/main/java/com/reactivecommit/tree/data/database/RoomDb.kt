package com.reactivecommit.tree.data.database

import androidx.room.*
import com.reactivecommit.tree.data.*

@androidx.room.Database(
    entities = [House::class, Character::class, Relative::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RootDatabase : RoomDatabase() {
    abstract fun houseDao(): HouseDao
    abstract fun characterDao(): CharacterDao
}

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> = value.split(ARRAY_SEPARATOR)

    @TypeConverter
    fun fromArrayList(list: List<String>) = list.joinToString(ARRAY_SEPARATOR)

    companion object {
        private const val ARRAY_SEPARATOR = "^#$&%"
    }
}

interface Database {
    suspend fun insertHouses(houses: List<HouseRes>)
    suspend fun insertCharacters(characters: List<CharacterRes>)
    suspend fun dropDb()
    suspend fun findCharactersByHouseName(name: String): List<CharacterItem>
    suspend fun findCharacterFullById(id: String): CharacterFull
    suspend fun getCountHouses(): Int
    suspend fun getHouses(): List<HouseItem>
}

class RoomDb(
    private val houseDao: HouseDao,
    private val characterDao: CharacterDao
) : Database {

    override suspend fun insertHouses(houses: List<HouseRes>) {
        houseDao.insert(*houses.map { it.toEntity() }.toTypedArray())
        houseDao.insertRelative(
            *houses.map { house ->
                house.swornMembers.map { member ->
                    Relative(member.getLastSegment(), house.url.getLastSegment())
                }
            }.flatten().toTypedArray()
        )
    }

    override suspend fun getHouses(): List<HouseItem> {
        return houseDao.getAll().map { it.toHouse() }
    }

    override suspend fun insertCharacters(characters: List<CharacterRes>) {
        characterDao.insert(*characters.map { characterRes ->
            characterRes.toEntity()
        }.toTypedArray())
    }

    override suspend fun dropDb() {
        houseDao.removeAll()
        characterDao.removeAll()
    }

    override suspend fun findCharactersByHouseName(name: String): List<CharacterItem> {
        return characterDao.getByHouseName(name).map { it.toCharacterItem() }
    }

    override suspend fun findCharacterFullById(id: String): CharacterFull {
        return characterDao.getById(id)!!.toCharacterFull(characterDao)
    }

    override suspend fun getCountHouses(): Int = houseDao.getCount()

}

private fun House.toHouse(): HouseItem {
    return HouseItem(
        id,
        name,
        region,
        coatOfArms,
        words,
        titles,
        seats,
        currentLord,
        heir,
        overlord,
        founded,
        founder,
        diedOut,
        ancestralWeapons
    )
}

private fun CharacterWithHouse.toCharacterItem() = CharacterItem(
    id, house, name, titles, aliases
)


private fun CharacterWithHouse.toCharacterFull(characterDao: CharacterDao): CharacterFull {
    val father = characterDao.getById(this.father)
    val mother = characterDao.getById(this.mother)
    return CharacterFull(
        id, name, words, born, died, titles, aliases, house,
        father?.let { RelativeCharacter(it.id, it.name, it.house) },
        mother?.let { RelativeCharacter(it.id, it.name, it.house) }
    )
}


private fun String.getLastSegment(delimiters: String = "/") = this.split(delimiters).last()

private fun CharacterRes.toEntity(): Character {
    return Character(
        url.getLastSegment(),
        name,
        born,
        died,
        titles,
        aliases,
        father = father.getLastSegment(),
        mother = mother.getLastSegment()
    )
}

private fun HouseRes.toEntity() = House(
    url.getLastSegment(),
    getShortHouseName(name),
    region,
    coatOfArms,
    words,
    titles,
    seats,
    currentLord,
    heir,
    overlord,
    founded,
    founder,
    diedOut,
    ancestralWeapons
)

fun getShortHouseName(houseName: String) =
    houseName.replace("^House ".toRegex(), "").replace(" of .*".toRegex(), "")