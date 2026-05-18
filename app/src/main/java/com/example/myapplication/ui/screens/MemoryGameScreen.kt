package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.models.KidioSpeech
import com.example.myapplication.models.categories
import com.example.myapplication.ui.components.MemoryCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryGameScreen(onBackClick: () -> Unit, speech: KidioSpeech) {
    val category = categories[0]
    val gameItems = remember {
        (category.items.take(6) + category.items.take(6))
            .shuffled()
            .toMutableStateList()
    }
    var flippedIndices by remember { mutableStateOf(setOf<Int>()) }
    var matchedIndices by remember { mutableStateOf(setOf<Int>()) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Memory Match", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Find all pairs!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(gameItems) { index, item ->
                    val isMatched = matchedIndices.contains(index)
                    val isFlipped = flippedIndices.contains(index) || isMatched

                    MemoryCard(
                        item = item,
                        isFlipped = isFlipped,
                        onClick = {
                            if (!isFlipped && flippedIndices.size < 2) {
                                flippedIndices = flippedIndices + index
                                speech.say(item.name)

                                if (flippedIndices.size == 2) {
                                    scope.launch {
                                        delay(1000)
                                        val first = flippedIndices.first()
                                        val second = flippedIndices.last()
                                        if (gameItems[first] == gameItems[second]) {
                                            matchedIndices = matchedIndices + first + second
                                            speech.prompt("Match found!")
                                        }
                                        flippedIndices = emptySet()
                                    }
                                }
                            }
                        }
                    )
                }
            }

            if (matchedIndices.size == gameItems.size && gameItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Text("YOU WON! 🏆", style = MaterialTheme.typography.headlineLarge, color = Color(0xFF4CAF50), fontWeight = FontWeight.Black)
                Button(onClick = {
                    matchedIndices = emptySet()
                    gameItems.shuffle()
                }) {
                    Text("Play Again")
                }
            }
        }
    }
}
