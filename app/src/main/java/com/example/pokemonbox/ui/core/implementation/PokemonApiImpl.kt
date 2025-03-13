package com.example.pokemonbox.ui.core.implementation

import com.example.pokemonbox.ui.Pokemon
import retrofit2.Response
import retrofit2.http.GET

interface PokemonApiImpl {
    @GET("pokemon")
    suspend fun getPokemonList(): Response<List<Pokemon>>
}