package com.example.myapplication.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LightbulbCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.models.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LetterBuildScreen(onBackClick: () -> Unit, speech: KidioSpeech) {
    var currentItem by remember { mutableStateOf(randomBuildableItem()) }
    val target = currentItem.spellingWord()
    var selectedSlots by remember(currentItem.name) { mutableStateOf(List<Char?>(target.length) { null }) }
    var letterPool by remember(currentItem.name) { mutableStateOf(target.toList().shuffled()) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var feedbackOk by remember { mutableStateOf(false) }
    var completedCount by remember { mutableIntStateOf(0) }
    var showHint by remember { mutableStateOf(false) }

    fun resetSlots() {
        val word = currentItem.spellingWord()
        selectedSlots = List(word.length) { null }
        letterPool = word.toList().shuffled()
        feedback = null
        feedbackOk = false
        showHint = false
    }

    fun loadRandomWord() {
        currentItem = randomBuildableItem(exclude = currentItem)
        feedback = null
        feedbackOk = false
        showHint = false
    }

    LaunchedEffect(currentItem.name) {
        speech.prompt("Bé hãy ghép các chữ để tạo từ: ${currentItem.displayHint()}")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ghép Chữ 🔤", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        showHint = !showHint
                        if (showHint) {
                            speech.prompt("Gợi ý: ${currentItem.speakLabel()}")
                        }
                    }) {
                        Icon(
                            if (showHint) Icons.Default.LightbulbCircle else Icons.Default.Lightbulb,
                            contentDescription = "Gợi ý",
                            tint = if (showHint) Color(0xFFFFC107) else Color.White
                        )
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
            Text(
                "Từ ngẫu nhiên từ Let's Learn",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (currentItem.isDigitVisual()) {
                Text(
                    currentItem.emoji,
                    fontSize = 96.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF009688)
                )
            } else {
                Text(currentItem.emoji, fontSize = 80.sp)
            }
            
            // Hiển thị chữ tiếng Anh chỉ khi bấm gợi ý
            if (showHint) {
                Text(
                    currentItem.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F)
                )
            } else {
                Text(
                    "?????",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
            }
            
            Text(
                currentItem.displayHint(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF009688),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                selectedSlots.forEachIndexed { index, letter ->
                    val filled = letter != null
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(0.85f)
                            .clickable(enabled = filled) {
                                if (letter != null) {
                                    letterPool = letterPool + letter
                                    selectedSlots = selectedSlots.toMutableList().also {
                                        it[index] = null
                                    }
                                    feedback = null
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (filled) Color(0xFFB2DFDB) else Color(0xFFE0E0E0)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (filled) 6.dp else 2.dp)
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = letter?.toString() ?: "?",
                                fontSize = if (target.length > 6) 20.sp else 28.sp,
                                fontWeight = FontWeight.Black,
                                color = if (filled) Color(0xFF00695C) else Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Chọn chữ:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            val rows = letterPool.chunked(6)
            rows.forEach { rowLetters ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    rowLetters.forEach { ch ->
                        Button(
                            onClick = {
                                val emptyIndex = selectedSlots.indexOfFirst { it == null }
                                if (emptyIndex >= 0) {
                                    selectedSlots = selectedSlots.toMutableList().also {
                                        it[emptyIndex] = ch
                                    }
                                    letterPool = letterPool.toMutableList().also { list ->
                                        list.removeAt(list.indexOf(ch))
                                    }
                                    feedback = null
                                }
                            },
                            modifier = Modifier.size(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688))
                        ) {
                            Text(ch.toString(), fontSize = 22.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { resetSlots(); speech.prompt("Làm lại nhé") },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(3.dp, Color(0xFF009688))
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Làm lại", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        val built = selectedSlots.joinToString("") { it?.toString() ?: "" }
                        if (built.length < target.length) {
                            feedback = "Bé ghép đủ chữ đã nhé!"
                            feedbackOk = false
                            speech.prompt("Bé ghép đủ chữ đã nhé!")
                        } else if (built.equals(target, ignoreCase = true)) {
                            feedback = "Đúng rồi! Giỏi quá! 🌟"
                            feedbackOk = true
                            completedCount++
                            speech.prompt("Đúng rồi! Từ là ${currentItem.name}")
                        } else {
                            feedback = "Chưa đúng, thử lại nhé! 💪"
                            feedbackOk = false
                            speech.prompt("Chưa đúng, thử lại nhé!")
                        }
                    },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Kiểm tra", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Đúng: $completedCount", fontWeight = FontWeight.Bold, color = Color(0xFF009688))
                Button(
                    onClick = {
                        loadRandomWord()
                        speech.prompt("Từ mới: ${currentItem.displayHint()}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688))
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Từ mới")
                }
            }
        }
    }
}
