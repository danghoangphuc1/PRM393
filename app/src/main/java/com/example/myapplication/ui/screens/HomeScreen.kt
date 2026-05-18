package com.example.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.models.Category
import com.example.myapplication.models.categories
import com.example.myapplication.ui.components.CategoryCard
import com.example.myapplication.ui.components.GameCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    promptVoiceEnabled: Boolean,
    onPromptVoiceChange: (Boolean) -> Unit,
    onCategoryClick: (Category) -> Unit,
    onGameClick: (String) -> Unit,
    onWritingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Kidio Learning",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        letterSpacing = 1.sp
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (promptVoiceEnabled) {
                                Icons.AutoMirrored.Filled.VolumeUp
                            } else {
                                Icons.Default.VolumeOff
                            },
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Switch(
                            checked = promptVoiceEnabled,
                            onCheckedChange = onPromptVoiceChange
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        containerColor = Color(0xFFFDFCF4)
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Special Writing Practice Button
            Card(
                modifier = Modifier.fillMaxWidth().height(80.dp).clickable { onWritingClick() },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF673AB7)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, null, Modifier.size(32.dp), Color.White)
                    Spacer(Modifier.width(12.dp))
                    Text("Bé Tập Viết ✍️", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }

            if (!promptVoiceEnabled) {
                Text(
                    text = "🔇 Đã tắt giọng hướng dẫn — bấm loa trên thẻ để nghe từ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF795548),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Fun Games! 🎮",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color(0xFFE91E63),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GameCard(
                    name = "Memory",
                    icon = Icons.Default.Extension,
                    color = Color(0xFF673AB7),
                    modifier = Modifier.weight(1f),
                    onClick = { onGameClick("memory_game") }
                )
                GameCard(
                    name = "Balloons",
                    icon = Icons.Default.Celebration,
                    color = Color(0xFFFF4081),
                    modifier = Modifier.weight(1f),
                    onClick = { onGameClick("balloon_pop") }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GameCard(
                    name = "Ghép chữ",
                    icon = Icons.Default.TextFields,
                    color = Color(0xFF009688),
                    modifier = Modifier.weight(1f),
                    onClick = { onGameClick("letter_build") }
                )
                GameCard(
                    name = "Nghe chọn",
                    icon = Icons.Default.Hearing,
                    color = Color(0xFF5C6BC0),
                    modifier = Modifier.weight(1f),
                    onClick = { onGameClick("listen_choose") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Let's Learn! 📚",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color(0xFF5D4037),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(category, onClick = { onCategoryClick(category) })
                }
            }
        }
    }
}
