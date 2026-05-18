package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.models.KidioSpeech
import com.example.myapplication.models.listenChooseOptions
import com.example.myapplication.models.randomLearnItem
import com.example.myapplication.ui.components.ListenChooseOptionCard
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListenChooseScreen(onBackClick: () -> Unit, speech: KidioSpeech) {
    var currentItem by remember { mutableStateOf(randomLearnItem()) }
    var options by remember(currentItem.name) { mutableStateOf(listenChooseOptions(currentItem)) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var feedbackOk by remember { mutableStateOf(false) }
    var showRetry by remember { mutableStateOf(false) }
    var answeredCorrectly by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    fun speakQuestion() {
        speech.say(currentItem.name)
    }

    fun loadNextQuestion() {
        currentItem = randomLearnItem(exclude = currentItem)
        options = listenChooseOptions(currentItem)
        feedback = null
        feedbackOk = false
        showRetry = false
        answeredCorrectly = false
    }

    LaunchedEffect(currentItem.name) {
        delay(400)
        speakQuestion()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nghe & Chọn 👂", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5C6BC0),
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Điểm: $score",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color(0xFF5C6BC0),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("👂", fontSize = 48.sp)
                    Text(
                        "Bé nghe và chọn đúng từ nhé!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FilledIconButton(
                        onClick = { speakQuestion() },
                        modifier = Modifier.size(72.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFF5C6BC0)
                        )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Nghe lại",
                            modifier = Modifier.size(40.dp),
                            tint = Color.White
                        )
                    }
                    Text(
                        "Bấm loa để nghe lại",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(options, key = { it.name }) { item ->
                    ListenChooseOptionCard(
                        item = item,
                        enabled = !answeredCorrectly,
                        isCorrectHighlight = answeredCorrectly && item.name == currentItem.name,
                        onClick = {
                            if (answeredCorrectly) return@ListenChooseOptionCard
                            if (item.name == currentItem.name) {
                                feedback = "Đúng rồi! Bé giỏi quá! 🌟"
                                feedbackOk = true
                                showRetry = false
                                answeredCorrectly = true
                                score++
                                speech.prompt("Đúng rồi! Bé giỏi quá!")
                            } else {
                                feedback = "Chưa đúng rồi!"
                                feedbackOk = false
                                showRetry = true
                                speech.prompt("Sai rồi, bé thử lại nhé!")
                            }
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                feedback?.let { msg ->
                    Text(
                        msg,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (feedbackOk) Color(0xFF4CAF50) else Color(0xFFF44336),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (showRetry && !answeredCorrectly) {
                    Button(
                        onClick = { speakQuestion() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Thử lại — nghe lại", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }

                if (answeredCorrectly) {
                    Button(
                        onClick = {
                            loadNextQuestion()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Từ tiếp theo", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
