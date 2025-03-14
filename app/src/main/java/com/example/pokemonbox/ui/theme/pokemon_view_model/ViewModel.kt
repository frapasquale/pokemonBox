package com.example.pokemonbox.ui.theme.pokemon_view_model

import ApiClient
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonbox.ui.core.model.BasePokemon
import com.example.pokemonbox.ui.core.model.PokeModel
import com.example.pokemonbox.ui.core.model.Pokemon
import com.example.pokemonbox.ui.core.model.Sprites
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {

    var pokemons = mutableStateOf<List<PokeModel>>(emptyList())
        private set

    private var pokemonList = mutableStateOf<List<Pokemon>>(emptyList())
        private set

    private var pokemonDescription = mutableStateOf<String?>(null)
        private set

    private var basePokemon = mutableStateOf<List<BasePokemon>>(emptyList())

    var isLoading = mutableStateOf(true)
        private set

    private var currentPage = 0

    init {
        Log.d("PokemonViewModel", "init")
        fetchPokemon()
    }

     fun fetchPokemon() {
        Log.d("PokemonViewModel", "fetchPokemon")
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = ApiClient.apiService.getPokemonList(offset = currentPage * 20)
                Log.d("PokemonViewModel", "fetchPokemon: ${response.results}")
                basePokemon.value += response.results
                fetchPokemonDetail()
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun fetchPokemonDetail() {
        Log.d("PokemonViewModel", "fetchPokemonDetail ${basePokemon.value}")
        viewModelScope.launch {
            try {
                isLoading.value = true
                Log.d("PokemonViewModel", "fetchPokemonDetail_2: ${basePokemon.value}")
                basePokemon.value.forEachIndexed { index, _ ->
                    Log.d("PokemonViewModel", "fetchPokemonDetail_3: ${(index * currentPage) + 1}")
                    val response = ApiClient.apiService.getPokemonDetail(((index * currentPage) + 1).toString())
                    val descriptionResponse = ApiClient.apiService.getPokemonDescription(((index * currentPage) + 1).toString())
                    Log.d("PokemonViewModel", "fetchPokemonDetail_4: $response")
                    pokemonList.value += response
                    pokemonDescription.value = descriptionResponse.flavor_text_entries.firstOrNull { it.language.name == "it" }?.flavor_text

                    pokemons.value += PokeModel(
                        name = response.name,
                        description = pokemonDescription.value ?: "",
                        url = response.sprites.front_default,
                        id = response.id,
                        height = response.height,
                        weight = response.weight,
                        types = response.types,
                        sprites = Sprites(
                            front_default = response.sprites.front_default,
                            back_default = response.sprites.back_default
                        )
                    )




                }

            } catch (e: Exception) {
                Log.d("PokemonViewModel", "fetchPokemonDetail_5: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }
}