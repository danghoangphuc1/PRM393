package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.myapplication.ui.components.ItemCard
import com.example.myapplication.ui.components.LearnQuizModeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    category: Category,
    onBackClick: () -> Unit,
    onItemClick: (KidioItem) -> Unit,
    onQuizClick: () -> Unit,
    speech: KidioSpeech
) {
    var mode by remember { mutableStateOf("learn") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category.name, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp)
                        )
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
                .background(category.color.copy(alpha = 0.06f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LearnQuizModeCard(
                    selected = mode == "learn",
                    title = "Học",
                    emoji = "📖",
                    subtitle = "Xem & nghe",
                    color = Color(0xFF43A047),
                    modifier = Modifier.weight(1f),
                    onClick = { mode = "learn" }
                )
                LearnQuizModeCard(
                    selected = mode == "quiz",
                    title = "Quiz",
                    emoji = "🎯",
                    subtitle = "Chơi đố",
                    color = Color(0xFFFF6F00),
                    modifier = Modifier.weight(1f),
                    onClick = { mode = "quiz" }
                )
            }

            when (mode) {
                "learn" -> {
                    Text(
                        text = "Chạm vào hình để nghe tên nhé! 🔊",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(category.items) { item ->
                            ItemCard(
                                item = item,
                                onClick = { onItemClick(item) },
                                onSpeakClick = {
                                    val text = if (item.description.isNotBlank()) item.description else item.name
                                    speech.say(text)
                                }
                            )
                        }
                    }
                }
                else -> {
                    QuizIntroPanel(
                        category = category,
                        onStartQuiz = onQuizClick,
                        speech = speech
                    )
                }
            }
        }
    }
}

@Composable
fun QuizIntroPanel(
    category: Category,
    onStartQuiz: () -> Unit,
    speech: KidioSpeech
) {
    LaunchedEffect(Unit) {
        speech.prompt("Bé chọn Quiz để chơi đố vui nhé!")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎯", fontSize = 72.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Đố vui: ${category.name}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = category.color,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tìm đúng hình theo tên!\nCó ${category.items.size} câu hỏi thú vị",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                speech.prompt("Bắt đầu Quiz!")
                onStartQuiz()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = category.color)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Bắt đầu Quiz!", fontSize = 24.sp, fontWeight = FontWeight.Black)
        }
    }
}
