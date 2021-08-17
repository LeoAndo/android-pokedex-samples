package com.example.pokedexcomposesample.pokemonstats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.pokedexcomposesample.domain.model.PokemonStatsModel

@Composable
fun PokemonStatsScreen(
    dominantColor: Color,
    id: Int,
    idWithName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    imageSize: Dp = 200.dp,
    viewModel: PokemonStatsViewModel = hiltViewModel()
) {
    val pokemonInfo = produceState<PokemonStatsModel?>(initialValue = null) {
        value = viewModel.getPokemonStats(id)
    }.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        PokemonTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        StateWrapper(
            info = pokemonInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + imageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            pokemonInfo?.sprites?.let {
                val painter = rememberImagePainter(data = it.front_default)
                Image(
                    painter = painter,
                    contentDescription = idWithName,
                    modifier = Modifier
                        .size(imageSize)
                        .offset(y = topPadding)
                )
            }
        }
    }
}

@Composable
fun PokemonTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(
                listOf(
                    Color.Black,
                    Color.Transparent
                )
            )
        ),
        contentAlignment = Alignment.TopStart
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack, contentDescription = null,
            tint = Color.White, modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun StateWrapper(
    info: PokemonStatsModel?,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {

}