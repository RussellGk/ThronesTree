package com.reactivecommit.tree.ui.houses

import androidx.lifecycle.*
import com.reactivecommit.tree.RootRepository
import com.reactivecommit.tree.data.CharacterItem
import com.reactivecommit.tree.data.HouseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class HouseViewModel(private val house: HouseType) : ViewModel(), KoinComponent {
    private val repository: RootRepository by inject()
    private val queryStr = MutableLiveData<String>("")

    fun getCharacters(): LiveData<List<CharacterItem>> =
        MediatorLiveData<List<CharacterItem>>().apply {
            viewModelScope.launch(Dispatchers.IO) {
                val characters = repository.findCharactersByHouseName(house.title)
                withContext(Dispatchers.Main) {
                    addSource(queryStr) { query ->
                        postValue(
                            if (query.isEmpty()) characters
                            else characters.filter { it.name.contains(query, true) }
                        )
                    }
                }
            }
        }

    fun handleSearchQuery(str: String) {
        queryStr.value = str
    }

}

class HouseViewModelFactory(private val house: HouseType) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HouseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HouseViewModel(house) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
