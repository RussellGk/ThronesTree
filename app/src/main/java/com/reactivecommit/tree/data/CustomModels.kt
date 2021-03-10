package com.reactivecommit.tree.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.reactivecommit.tree.R

enum class HouseType(
    val title: String,
    @DrawableRes
    val icon: Int,
    @DrawableRes
    val coastOfArms: Int,
    @ColorRes
    val primaryColor: Int,
    @ColorRes
    val accentColor: Int,
    @ColorRes
    val darkColor: Int
) {

    STARK("Stark", R.drawable.stark_icon, R.drawable.stark_coast_of_arms, R.color.stark_primary, R.color.stark_accent, R.color.stark_dark),
    LANNISTER("Lannister", R.drawable.lannister_icon, R.drawable.lannister_coast_of_arms, R.color.lannister_primary, R.color.lannister_accent, R.color.lannister_dark),
    TARGARYEN("Targaryen", R.drawable.targaryen_icon, R.drawable.targaryen_coast_of_arms, R.color.targaryen_primary, R.color.targaryen_accent, R.color.targaryen_dark),
    BARATHEON("Baratheon", R.drawable.baratheon_icon, R.drawable.baratheon_coast_of_arms, R.color.baratheon_primary, R.color.baratheon_accent, R.color.baratheon_dark),
    GREYJOY("Greyjoy", R.drawable.greyjoy_icon, R.drawable.greyjoy_coast_of_arms, R.color.greyjoy_primary, R.color.greyjoy_accent, R.color.greyjoy_dark),
    MARTELL("Martell", R.drawable.martel_icon, R.drawable.martel_coast_of_arms, R.color.martel_primary, R.color.martel_accent, R.color.martel_dark),
    TYRELL("Tyrell", R.drawable.tyrel_icon, R.drawable.tyrel_coast_of_arms, R.color.tyrel_primary, R.color.tyrel_accent, R.color.tyrel_dark);

    companion object {
        fun fromString(title: String) =
            when (title){
                "Stark" -> STARK
                "Lannister" -> LANNISTER
                "Targaryen" -> TARGARYEN
                "Baratheon" -> BARATHEON
                "Greyjoy" -> GREYJOY
                "Martell" -> MARTELL
                "Tyrell" -> TYRELL
                else -> STARK
            }
    }
}

data class CharacterItem(
    val id: String,
    val house: String, //rel
    val name: String,
    val titles: List<String>,
    val aliases: List<String>
)

data class CharacterFull(
    val id: String,
    val name: String,
    val words: String,
    val born: String,
    val died: String,
    val titles: List<String>,
    val aliases: List<String>,
    val house: String, //rel
    val father: RelativeCharacter?,
    val mother: RelativeCharacter?
)

data class RelativeCharacter(
    val id: String,
    val name: String,
    val house: String //rel
)

data class HouseItem(
    val id: String,
    val name: String,
    val region: String,
    val coatOfArms: String,
    val words: String,
    val titles: List<String>,
    val seats: List<String>,
    val currentLord: String, //rel
    val heir: String, //rel
    val overlord: String,
    val founded: String,
    val founder: String, //rel
    val diedOut: String,
    val ancestralWeapons: List<String>
)

data class CharacterRes(
    val url: String,
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String> = listOf(),
    val aliases: List<String> = listOf(),
    val father: String,
    val mother: String,
    val spouse: String,
    val allegiances: List<String> = listOf(),
    val books: List<String> = listOf(),
    val povBooks: List<Any> = listOf(),
    val tvSeries: List<String> = listOf(),
    val playedBy: List<String> = listOf()
){
    var houseId:String? = null
}

data class HouseRes(
    val url: String,
    val name: String,
    val region: String,
    val coatOfArms: String,
    val words: String,
    val titles: List<String> = listOf(),
    val seats: List<String> = listOf(),
    val currentLord: String,
    val heir: String,
    val overlord: String,
    val founded: String,
    val founder: String,
    val diedOut: String,
    val ancestralWeapons: List<String> = listOf(),
    val cadetBranches: List<Any> = listOf(),
    val swornMembers: List<String> = listOf()
)

