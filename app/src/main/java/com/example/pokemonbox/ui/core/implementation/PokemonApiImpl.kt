package com.example.pokemonbox.ui.core.implementation

import com.example.pokemonbox.ui.core.model.Pokemon
import com.example.pokemonbox.ui.core.model.PokemonDescription
import com.example.pokemonbox.ui.core.model.PokemonDetailResponse
import com.example.pokemonbox.ui.core.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiImpl {
    @GET("pokemon")
    suspend fun getPokemonList(@Query("offset") offset: Int): PokemonResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: String): Pokemon

    @GET("pokemon-species/{id}")
    suspend fun getPokemonDescription(@Path("id") id: String): PokemonDescription
}