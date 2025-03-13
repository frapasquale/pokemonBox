package com.example.pokemonbox.ui.core.client

import com.example.pokemonbox.ui.core.implementation.PokemonApiImpl
import android.util.Log
import com.example.pokemonbox.ui.Pokemon
import retrofit2.Response

internal class PokemonApiClient(
    private val pokemonApiImpl: PokemonApiImpl
) {
    suspend fun getPokemons(): Response<List<Pokemon>> {
        return pokemonApiImpl.getPokemonList()
    }

    private fun <T> Response<T>.asResult(): Result<T> {

        if (this.isSuccessful && this.body() != null) {
            return Result.success(this.body()!!)
        } else {

            Log.e("Error: ${this.errorBody()?.string()}", "Error: ${this.errorBody()?.string()}")
            throw Exception("Error: ${this.errorBody()?.string()}")
        }
    }
}