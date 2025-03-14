package com.example.pokemonbox.ui

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.android.volley.toolbox.ImageRequest
import com.example.pokemonbox.ui.core.model.PokeModel
import com.example.pokemonbox.ui.core.model.Pokemon
import com.example.pokemonbox.ui.core.model.PokemonDescription
import com.example.pokemonbox.ui.core.model.Type
import com.example.pokemonbox.ui.theme.pokemon_view_model.PokemonViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: PokemonViewModel = viewModel()
            PokemonBoxApp(viewModel)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonBoxApp(viewModel: PokemonViewModel = viewModel()) {

    val isLoading by viewModel.isLoading


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
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),

                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
                PokemonList(viewModel.pokemons.value)
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonList(pokemons: List<PokeModel>, viewModel: PokemonViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredPokemonList = pokemons.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.types.any { type ->
            type.type.name.lowercase(Locale.ROOT).contains(searchQuery, ignoreCase = true)
        }
    }
    val listState = rememberLazyListState()



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
fun PokemonItem(pokemon: PokeModel) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)

    ) {
        Column {
            Row(modifier = Modifier.padding(2.dp)) {

                val description = pokemon.description.replace("\n", " ")

                ImageCard(pokemon)

                Column(modifier = Modifier.padding(start = 2.dp)) {
                    Text(text = pokemon.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Box(modifier = Modifier.padding(top = 4.dp))
                    Row {
                        pokemon.types.forEach { type ->
                            TypeBadge(type)
                        }
                    }
                    Box(modifier = Modifier.padding(top = 4.dp))
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                   
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun ImageCard(pokemon: PokeModel) {

        Box(

            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(100.dp),
                model = pokemon.sprites.front_default,
                contentDescription = pokemon.name,

            )
        }

}

@Composable
fun TypeBadge(type: Type) {
    val color = when (type.type.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }) {
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
            text = type.type.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
        )
    }
}
