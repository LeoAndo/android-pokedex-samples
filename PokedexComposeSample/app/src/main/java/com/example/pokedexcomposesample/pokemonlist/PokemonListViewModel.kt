package com.example.pokedexcomposesample.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokedexcomposesample.data.api.response.toModels
import com.example.pokedexcomposesample.data.repository.PAGE_SIZE
import com.example.pokedexcomposesample.data.repository.PokemonRepository
import com.example.pokedexcomposesample.domain.model.PokemonModel
import com.example.pokedexkotlinsample.domain.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    var pokemons = mutableStateOf<List<PokemonModel>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getPokemons(PAGE_SIZE, curPage * PAGE_SIZE)) {
                is ApiResult.Success -> {
                    endReached.value = (curPage * PAGE_SIZE >= result.value.count)
                    pokemons.value = result.value.results.toModels()
                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                }
                is ApiResult.Failure -> {
                    loadError.value = result.errorBody?.toString() ?: "error"
                }
                is ApiResult.NetworkError -> {
                    loadError.value = "NetworkError!"
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let {
                onFinish(Color(it))
            }
        }
    }
}