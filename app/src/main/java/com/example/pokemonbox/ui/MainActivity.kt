package com.example.pokemonbox.ui

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

data class Pokemon(
    val name: String,
    val types: List<String>,
    val description: String,
)

val pokemonList = listOf(
    Pokemon("Bulbasaur", listOf("Grass", "Poison"), "It uses the nutrients that are packed on its back in order to grow."),
    Pokemon("Luxray", listOf("Electric"), "It can see clearly through walls to track down its prey and seek its lost young."),
    Pokemon("Shellder", listOf("Water"), "It is encased in a shell harder than diamond, but inside it is tender."),
    Pokemon("Rayquaza", listOf("Dragon", "Flying"), "Is said to have lived for hundreds of millions of years."),
    Pokemon("Gulpin", listOf("Poison"), "There is nothing its stomach can't digest."),
    Pokemon("Pikachu", listOf("Electric"), "It raises its tail to check its surroundings."),
    Pokemon("Charmander", listOf("Fire"), "It has a preference for hot things."),
    Pokemon("Squirtle", listOf("Water"), "After birth, its back swells and hardens into a shell."),
    Pokemon("Jigglypuff", listOf("Normal", "Fairy"), "It captivates foes with its huge, round eyes."),
    Pokemon("Gengar", listOf("Ghost", "Poison"), "It hides in shadows. It is said that if Gengar is hiding, it cools the area by nearly 10 degrees Fahrenheit."),
    Pokemon("Mewtwo", listOf("Psychic"), "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments."),
    Pokemon("Mew", listOf("Psychic"), "So rare that it is still said to be a mirage by many experts."),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonBoxApp()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonBoxApp() {
    val systemUiController = rememberSystemUiController()


    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.White,
            darkIcons = true
        )
    }
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(title = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pokemon", fontWeight = FontWeight.Normal)
                    Text("Box", fontWeight = FontWeight.Bold)
                }
            })
        }
    ) {
        PokemonList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonList() {
    var searchQuery by remember { mutableStateOf("") }

    val filteredPokemonList = pokemonList.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.types.any { type -> type.contains(searchQuery, ignoreCase = true) }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp)
    ) {

        TextField(
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                containerColor = Color.White,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                disabledIndicatorColor = Color.Black
            ),
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp),
            placeholder = { Text("Search name or type") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cerca") }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(filteredPokemonList.take(20)) { pokemon ->
                PokemonItem(pokemon)
            }
        }
    }


}

@Composable
fun PokemonItem(pokemon: Pokemon) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp).background(Color.White)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = pokemon.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Box(modifier = Modifier.padding(top = 4.dp))
                Row {
                    pokemon.types.forEach { type ->
                        TypeBadge(type)
                    }
                }
                Box(modifier = Modifier.padding(top = 4.dp))
                Text(text = pokemon.description, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun TypeBadge(type: String) {
    val color = when (type) {
        "Grass" -> Color(0xFF78C850)
        "Poison" -> Color(0xFFA040A0)
        "Electric" -> Color(0xFFF8D030)
        "Water" -> Color(0xFF6890F0)
        "Dragon" -> Color(0xFF7038F8)
        "Flying" -> Color(0xFFA890F0)
        "Fire" -> Color(0xFFF08030)
        "Normal" -> Color(0xFFA8A878)
        "Fairy" -> Color(0xFFEE99AC)
        "Ghost" -> Color(0xFF705898)
        "Psychic" -> Color(0xFFF85888)
        "Bug" -> Color(0xFFA8B820)
        "Ground" -> Color(0xFFE0C068)
        "Rock" -> Color(0xFFB8A038)
        "Steel" -> Color(0xFFB8B8D0)
        "Ice" -> Color(0xFF98D8D8)
        "Dark" -> Color(0xFF705848)
        "Fight" -> Color(0xFFC03028)

        else -> Color.Gray
    }

    Card(
        modifier = Modifier.padding(end = 4.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Text(
            text = type,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
        )
    }
}
