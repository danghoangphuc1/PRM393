package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.models.KidioSpeech
import kotlinx.coroutines.delay
import kotlin.random.Random

data class Balloon(val id: Int, val x: androidx.compose.ui.unit.Dp, val y: androidx.compose.ui.unit.Dp, val color: Color, val speed: Float)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalloonPopScreen(onBackClick: () -> Unit, speech: KidioSpeech) {
    var score by remember { mutableIntStateOf(0) }
    val balloons = remember { mutableStateListOf<Balloon>() }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(Unit) {
        while (true) {
            if (balloons.size < 8) {
                balloons.add(
                    Balloon(
                        id = Random.nextInt(),
                        x = Random.nextInt(50, 300).dp,
                        y = screenHeight + 100.dp,
                        color = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta).random(),
                        speed = Random.nextFloat() * 2f + 1f
                    )
                )
            }
            delay(1000)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val toRemove = mutableListOf<Int>()
            for (i in balloons.indices) {
                val balloon = balloons[i]
                balloons[i] = balloon.copy(y = balloon.y - (balloon.speed).dp)
                if (balloons[i].y < (-100).dp) {
                    toRemove.add(i)
                }
            }
            toRemove.reversed().forEach { balloons.removeAt(it) }
            delay(16)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Balloon Pop 🎈", fontWeight = FontWeight.Bold) }, navigationIcon = {
                IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
            })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(0xFF87CEEB))) {
            Text(
                "Score: $score",
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            balloons.forEach { balloon ->
                Box(
                    modifier = Modifier
                        .offset(x = balloon.x, y = balloon.y)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(balloon.color)
                        .clickable {
                            speech.prompt("Pop!")
                            score++
                            balloons.remove(balloon)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(20.dp).align(Alignment.TopStart).padding(4.dp).background(Color.White.copy(alpha = 0.3f), CircleShape))
                }
            }
        }
    }
}
