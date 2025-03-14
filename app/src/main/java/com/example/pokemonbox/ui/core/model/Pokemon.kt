package com.example.pokemonbox.ui.core.model

data class PokemonResponse(
    val results: List<BasePokemon>
)

data class PokemonDetailResponse(
    val results: Pokemon
)

data class PokemonDescription(
    val flavor_text_entries: List<FlavorTextEntry>
)

data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language,
)

data class Language(
    val name: String,
    val url: String,
)

data class BasePokemon(
    val name: String,
    val url: String,
)

data class Pokemon(
    val name: String,
    val url: String,
    val id: Int,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val sprites: Sprites,
)

class PokeModel(
    val name: String,
    val description: String,
    val url: String,
    val id: Int,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val sprites: Sprites,
)

data class Sprites(
    val front_default: String,
    val back_default: String,
)

data class Type(
    val slot: Int,
    val type: TypeDetail,
)

data class TypeDetail(
    val name: String,
    val url: String,
)

