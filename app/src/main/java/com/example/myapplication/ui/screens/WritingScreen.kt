package com.example.myapplication.ui.screens

import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Paint as AndroidPaint
import android.graphics.Matrix as AndroidMatrix
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.models.KidioSpeech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritingScreen(onBackClick: () -> Unit, speech: KidioSpeech) {
    val characters = remember { ('A'..'Z').map { it.toString() } + (0..9).map { it.toString() } }
    var currentIndex by remember { mutableIntStateOf(0) }
    val paths = remember { mutableStateListOf<Path>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    
    // States for evaluation
    var score by remember { mutableFloatStateOf(0f) }
    var showScore by remember { mutableStateOf(false) }
    var rewardEmoji by remember { mutableStateOf("") }
    val rewards = listOf("🌟", "🌈", "🎈", "🏆", "🐱", "🍦", "🍭", "🎨")
    
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var isCalculating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tập Viết: ${characters[currentIndex]}") },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = {
                    IconButton(onClick = { 
                        paths.clear() 
                        showScore = false
                        score = 0f
                    }) { Icon(Icons.Default.Delete, "Xóa", tint = Color.Red) }
                    IconButton(onClick = { speech.say(characters[currentIndex]) }) { Icon(Icons.AutoMirrored.Filled.VolumeUp, null) }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp))
                    .onSizeChanged { canvasSize = it }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                showScore = false
                                currentPath = Path().apply { moveTo(offset.x, offset.y) }
                            },
                            onDrag = { change, _ ->
                                currentPath?.lineTo(change.position.x, change.position.y)
                                // Force update state to redraw
                                val p = currentPath
                                currentPath = null
                                currentPath = p
                            },
                            onDragEnd = {
                                currentPath?.let { paths.add(it) }
                                currentPath = null
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                // Background character template
                Text(
                    text = characters[currentIndex],
                    fontSize = 250.sp,
                    fontWeight = FontWeight.Thin,
                    color = Color.LightGray.copy(alpha = 0.3f),
                    modifier = Modifier.align(Alignment.Center)
                )

                // Drawing Canvas with GraphicsLayer for performance
                Canvas(modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer()
                ) {
                    paths.forEach { path ->
                        drawPath(path, color = Color(0xFF2196F3), style = Stroke(width = 20f, cap = StrokeCap.Round))
                    }
                    currentPath?.let { path ->
                        drawPath(path, color = Color(0xFF2196F3), style = Stroke(width = 20f, cap = StrokeCap.Round))
                    }
                }

                if (isCalculating) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = (showScore && score >= 7.0f),
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Text(rewardEmoji, fontSize = 80.sp)
                }

                // Score display
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.AnimatedVisibility(visible = showScore) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (score >= 7.0f) Color(0xFF4CAF50) else Color(0xFFFF9800),
                            shadowElevation = 6.dp
                        ) {
                            Text(
                                text = String.format(Locale.getDefault(), "%.1f / 10 điểm", score),
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {
                        if (paths.isNotEmpty() && canvasSize.width > 0 && !isCalculating) {
                            isCalculating = true
                            val pathsSnapshot = paths.toList()
                            val charToCalc = characters[currentIndex]
                            val w = canvasSize.width.toFloat()
                            val h = canvasSize.height.toFloat()
                            
                            scope.launch {
                                val resultScore = withContext(Dispatchers.Default) {
                                    calculateHandwritingScore(
                                        char = charToCalc,
                                        paths = pathsSnapshot,
                                        width = w,
                                        height = h
                                    )
                                }
                                score = resultScore
                                rewardEmoji = rewards.random()
                                showScore = true
                                isCalculating = false
                                
                                when {
                                    score <= 1.0f -> speech.prompt("Con viết sai chữ rồi, thử lại nhé!")
                                    score < 5.0f -> speech.prompt(String.format(Locale.getDefault(), "Nét viết của con chưa chuẩn lắm, được %.1f điểm. Cố lên!", score))
                                    score < 8.0f -> speech.prompt(String.format(Locale.getDefault(), "Con viết gần đúng rồi, được %.1f điểm. Ráng lên chút nữa!", score))
                                    else -> speech.prompt(String.format(Locale.getDefault(), "Xuất sắc! Con viết rất đẹp, được %.1f điểm!", score))
                                }
                            }
                        } else if (!isCalculating) {
                            speech.prompt("Bé hãy viết vào khung nhé")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    enabled = !isCalculating
                ) {
                    if (isCalculating) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Check, null)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(if (isCalculating) "Đang chấm..." else "Kiểm tra")
                }
            }

            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { 
                    if (currentIndex > 0) currentIndex--
                    paths.clear()
                    showScore = false
                    score = 0f
                    speech.say(characters[currentIndex])
                }, enabled = !isCalculating) { Text("Trước") }
                
                Text("${currentIndex + 1} / ${characters.size}", Modifier.padding(horizontal = 24.dp), fontWeight = FontWeight.Bold)

                Button(onClick = { 
                    if (currentIndex < characters.size - 1) currentIndex++
                    paths.clear()
                    showScore = false
                    score = 0f
                    speech.say(characters[currentIndex])
                }, enabled = !isCalculating) { Text("Tiếp") }
            }
        }
    }
}

/**
 * Thuật toán tính điểm viết tay: Precision & Grid Coverage.
 * 1. Precision: Tránh việc vẽ sai chữ bằng cách phạt nặng nét vẽ ngoài mẫu.
 * 2. Grid Coverage: Không phạt nét vẽ mảnh, chỉ cần bé đi qua đủ các vùng của chữ mẫu.
 */
fun calculateHandwritingScore(
    char: String,
    paths: List<Path>,
    width: Float,
    height: Float
): Float {
    val size = 128 
    val bitmapTemplate = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val bitmapUser = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    
    val canvasTemplate = AndroidCanvas(bitmapTemplate)
    val canvasUser = AndroidCanvas(bitmapUser)
    
    // 1. Vẽ chữ mẫu
    val paintTemplate = AndroidPaint().apply {
        color = android.graphics.Color.BLACK
        textSize = size * 0.85f
        textAlign = AndroidPaint.Align.CENTER
        typeface = Typeface.DEFAULT
        isAntiAlias = true
        style = AndroidPaint.Style.FILL_AND_STROKE
        strokeWidth = size * 0.15f // Vùng đích đủ rộng
    }
    
    val x = size / 2f
    val y = (size / 2f) - ((paintTemplate.descent() + paintTemplate.ascent()) / 2f)
    canvasTemplate.drawText(char, x, y, paintTemplate)
    
    // 2. Vẽ nét của người dùng
    val paintUser = AndroidPaint().apply {
        color = android.graphics.Color.BLACK
        style = AndroidPaint.Style.STROKE
        strokeWidth = size * 0.08f 
        strokeCap = AndroidPaint.Cap.ROUND
        strokeJoin = AndroidPaint.Join.ROUND
        isAntiAlias = true
    }
    
    val scaleX = size.toFloat() / width
    val scaleY = size.toFloat() / height
    
    paths.forEach { path ->
        val androidPath = path.asAndroidPath()
        val matrix = AndroidMatrix()
        matrix.setScale(scaleX, scaleY)
        val scaledPath = android.graphics.Path()
        androidPath.transform(matrix, scaledPath)
        canvasUser.drawPath(scaledPath, paintUser)
    }
    
    // 3. Tính toán Precision (Pixel-level)
    var intersectionPixels = 0
    var userPixels = 0
    
    for (i in 0 until size) {
        for (j in 0 until size) {
            val isTemplate = android.graphics.Color.alpha(bitmapTemplate.getPixel(i, j)) > 50
            val isUser = android.graphics.Color.alpha(bitmapUser.getPixel(i, j)) > 50
            
            if (isUser) {
                userPixels++
                if (isTemplate) intersectionPixels++
            }
        }
    }
    
    val precision = if (userPixels > 0) intersectionPixels.toFloat() / userPixels else 0f
    
    // 4. Tính toán Grid Coverage (12x12 grid)
    val gridSize = 12
    var totalTargetCells = 0
    var visitedTargetCells = 0
    
    for (row in 0 until gridSize) {
        for (col in 0 until gridSize) {
            val startX = col * size / gridSize
            val endX = (col + 1) * size / gridSize
            val startY = row * size / gridSize
            val endY = (row + 1) * size / gridSize
            
            var cellHasTemplate = false
            var cellHasUser = false
            
            // Check if this grid cell contains template or user pixels
            pixelLoop@for (py in startY until endY) {
                for (px in startX until endX) {
                    if (android.graphics.Color.alpha(bitmapTemplate.getPixel(px, py)) > 50) cellHasTemplate = true
                    if (android.graphics.Color.alpha(bitmapUser.getPixel(px, py)) > 50) cellHasUser = true
                    if (cellHasTemplate && cellHasUser) break@pixelLoop
                }
            }
            
            if (cellHasTemplate) {
                totalTargetCells++
                if (cellHasUser) visitedTargetCells++
            }
        }
    }
    
    bitmapTemplate.recycle()
    bitmapUser.recycle()
    
    if (totalTargetCells == 0 || userPixels == 0) return 0f
    
    val coverage = visitedTargetCells.toFloat() / totalTargetCells
    
    // 5. Công thức điểm cuối cùng: Precision * Min(1.0, Coverage / 0.85) * 10
    // Precision triệt tiêu việc vẽ sai chữ.
    // Coverage theo lưới đảm bảo không phạt nét vẽ mảnh.
    val finalCoverage = (coverage / 0.85f).coerceAtMost(1.0f)
    var finalScore = precision * finalCoverage * 10f
    
    // Phạt thêm nếu Precision quá thấp (vẽ sai chữ hoàn toàn)
    if (precision < 0.3f) finalScore *= 0.5f
    if (precision < 0.15f) finalScore = 0f

    finalScore = finalScore.coerceIn(0f, 10f)
    return (kotlin.math.round(finalScore * 10) / 10f)
}
