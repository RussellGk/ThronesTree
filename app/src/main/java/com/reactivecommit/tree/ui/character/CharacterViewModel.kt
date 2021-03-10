package com.reactivecommit.tree.ui.character

import androidx.lifecycle.*
import com.reactivecommit.tree.RootRepository
import com.reactivecommit.tree.data.CharacterFull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class CharacterViewModel(private val characterId: String) : ViewModel(), KoinComponent {

    private val rootRepository: RootRepository by inject()

    private val _character = MutableLiveData<CharacterFull>().apply {
        viewModelScope.launch(Dispatchers.IO) {
            postValue(
                rootRepository.findCharacterFullById(characterId)
            )
        }
    }

    fun getCharacter(): LiveData<CharacterFull> = _character
}


class CharacterViewModelFactory(private val characterId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CharacterViewModel(characterId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}