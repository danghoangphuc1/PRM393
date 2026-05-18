package com.example.myapplication.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.models.Category
import com.example.myapplication.models.KidioItem
import com.example.myapplication.models.KidioSpeech
import com.example.myapplication.ui.components.QuizOptionCard
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(category: Category, onBackClick: () -> Unit, speech: KidioSpeech) {
    var currentItem by remember { mutableStateOf(category.items.randomOrNull() ?: KidioItem("Empty", "")) }
    var options by remember { mutableStateOf(generateOptions(category, currentItem)) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var feedbackColor by remember { mutableStateOf(Color.Gray) }
    var score by remember { mutableIntStateOf(0) }
    var questionsAsked by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentItem) {
        if (currentItem.name != "Empty") {
            speech.prompt("Bé hãy tìm hình ${currentItem.name} ở đâu nào?")
        }
    }

    LaunchedEffect(feedback) {
        if (feedback != null) {
            delay(2000)
            feedback = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đố vui: ${category.name}", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = category.color,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Score: $score", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Question: ${questionsAsked + 1}", style = MaterialTheme.typography.titleLarge)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Where is the...",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Gray
                )
                Text(
                    currentItem.name.uppercase(),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = category.color,
                    fontSize = 48.sp
                )
                IconButton(onClick = { speech.say(currentItem.name) }) {
                    Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", modifier = Modifier.size(40.dp))
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(350.dp)
            ) {
                items(options) { item ->
                    QuizOptionCard(item) {
                        if (item == currentItem) {
                            feedback = "Đúng rồi! Bé giỏi quá! 🌟"
                            feedbackColor = Color(0xFF4CAF50)
                            score++
                            speech.prompt("Đúng rồi! Bé giỏi quá!")

                            val nextItem = category.items.randomOrNull() ?: currentItem
                            currentItem = nextItem
                            options = generateOptions(category, nextItem)
                        } else {
                            feedback = "Sai rồi, bé thử lại nhé! 💪"
                            feedbackColor = Color(0xFFF44336)
                            speech.prompt("Sai rồi, bé thử lại nhé!")
                        }
                        questionsAsked++
                    }
                }
            }

            Column(modifier = Modifier.height(80.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                AnimatedVisibility(
                    visible = feedback != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(
                        text = feedback ?: "",
                        style = MaterialTheme.typography.headlineSmall,
                        color = feedbackColor,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

fun generateOptions(category: Category, correctItem: KidioItem): List<KidioItem> {
    val otherItems = category.items.filter { it != correctItem }.shuffled().take(3)
    return (otherItems + correctItem).shuffled()
}
