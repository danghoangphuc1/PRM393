package com.example.myapplication

import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.layout.onSizeChanged
import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Paint as AndroidPaint
import android.graphics.Typeface
import android.graphics.Matrix as AndroidMatrix
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

data class Category(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val items: List<KidioItem> = emptyList()
)

data class KidioItem(
    val name: String,
    val emoji: String,
    val description: String = ""
)

val categories = listOf(
    Category(
        id = "animals",
        name = "Animals",
        icon = Icons.Default.Pets,
        color = Color(0xFFFF9800),
        items = listOf(
            KidioItem("Lion", "🦁"),
            KidioItem("Elephant", "🐘"),
            KidioItem("Giraffe", "🦒"),
            KidioItem("Monkey", "🐒"),
            KidioItem("Panda", "🐼"),
            KidioItem("Tiger", "🐯"),
            KidioItem("Zebra", "🦓"),
            KidioItem("Rabbit", "🐰")
        )
    ),
    Category(
        id = "alphabet",
        name = "Alphabet",
        icon = Icons.Default.TextFields,
        color = Color(0xFF2196F3),
        items = ('A'..'Z').map { KidioItem(it.toString(), it.toString()) }
    ),
    Category(
        id = "numbers",
        name = "Numbers",
        icon = Icons.Default.Numbers,
        color = Color(0xFF4CAF50),
        items = (1..20).map { KidioItem(it.toString(), it.toString()) }
    ),
    Category(
        id = "colors",
        name = "Colors",
        icon = Icons.Default.Palette,
        color = Color(0xFFE91E63),
        items = listOf(
            KidioItem("Red", "🔴"),
            KidioItem("Blue", "🔵"),
            KidioItem("Green", "🟢"),
            KidioItem("Yellow", "🟡"),
            KidioItem("Orange", "🟠"),
            KidioItem("Purple", "🟣"),
            KidioItem("Pink", "💗"),
            KidioItem("Brown", "🟤")
        )
    ),
    Category(
        id = "shapes",
        name = "Shapes",
        icon = Icons.Default.Category,
        color = Color(0xFF9C27B0),
        items = listOf(
            KidioItem("Circle", "⭕"),
            KidioItem("Square", "⬛"),
            KidioItem("Triangle", "🔺"),
            KidioItem("Star", "⭐"),
            KidioItem("Heart", "❤️"),
            KidioItem("Diamond", "💠")
        )
    ),
    Category(
        id = "fruits",
        name = "Fruits",
        icon = Icons.Default.Eco,
        color = Color(0xFFFF5722),
        items = listOf(
            KidioItem("Apple", "🍎"),
            KidioItem("Banana", "🍌"),
            KidioItem("Grapes", "🍇"),
            KidioItem("Strawberry", "🍓"),
            KidioItem("Watermelon", "🍉"),
            KidioItem("Pineapple", "🍍"),
            KidioItem("Mango", "🥭")
        )
    ),
    Category(
        id = "vehicles",
        name = "Vehicles",
        icon = Icons.Default.DirectionsCar,
        color = Color(0xFF607D8B),
        items = listOf(
            KidioItem("Car", "🚗"),
            KidioItem("Bus", "🚌"),
            KidioItem("Train", "🚂"),
            KidioItem("Airplane", "✈️"),
            KidioItem("Bicycle", "🚲"),
            KidioItem("Boat", "⛴️"),
            KidioItem("Rocket", "🚀")
        )
    ),
    Category(
        id = "body",
        name = "Body Parts",
        icon = Icons.Default.Face,
        color = Color(0xFF795548),
        items = listOf(
            KidioItem("Eyes", "👀"),
            KidioItem("Nose", "👃"),
            KidioItem("Ears", "👂"),
            KidioItem("Mouth", "👄"),
            KidioItem("Hands", "✋"),
            KidioItem("Feet", "👣")
        )
    )
)

@Composable
fun KidioApp() {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // TTS setup
            }
        }
        tts = ttsInstance
        ttsInstance.setLanguage(Locale("vi", "VN"))
        ttsReady = true
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
        }
    }

    val speak = { text: String ->
        if (ttsReady) {
            // Tự động chọn ngôn ngữ: Nếu chứa ký tự đặc biệt tiếng Việt hoặc là câu thoại dài -> Tiếng Việt. 
            // Nếu là tên các mục học tập đơn lẻ (thường là tiếng Anh) -> Tiếng Anh.
            val isEnglish = text.all { it in 'a'..'z' || it in 'A'..'Z' || it == ' ' } && text.length < 15
            
            if (isEnglish) {
                tts?.setLanguage(Locale.US)
            } else {
                tts?.setLanguage(Locale("vi", "VN"))
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        Unit
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("home") {
            HomeScreen(
                onCategoryClick = { category ->
                    speak(category.name)
                    navController.navigate("detail/${category.id}")
                },
                onGameClick = { gameRoute ->
                    speak("Chúng mình cùng chơi nhé!")
                    navController.navigate(gameRoute)
                },
                onWritingClick = {
                    speak("Bé hãy tập viết chữ và số nào!")
                    navController.navigate("writing")
                }
            )
        }
        composable(
            route = "detail/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            val category = categories.find { it.id == categoryId }
            if (category != null) {
                CategoryDetailScreen(
                    category = category,
                    onBackClick = { navController.popBackStack() },
                    onItemClick = { item -> speak(item.name) },
                    onQuizClick = { navController.navigate("quiz/${category.id}") }
                )
            }
        }
        composable(
            route = "quiz/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            val category = categories.find { it.id == categoryId }
            if (category != null) {
                QuizScreen(
                    category = category,
                    onBackClick = { navController.popBackStack() },
                    speak = speak
                )
            }
        }
        composable("memory_game") {
            MemoryGameScreen(onBackClick = { navController.popBackStack() }, speak = speak)
        }
        composable("balloon_pop") {
            BalloonPopScreen(onBackClick = { navController.popBackStack() }, speak = speak)
        }
        composable("writing") {
            WritingScreen(onBackClick = { navController.popBackStack() }, speak = speak)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
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
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GameCard(
                    name = "Memory Match",
                    icon = Icons.Default.Extension,
                    color = Color(0xFF673AB7),
                    modifier = Modifier.weight(1f),
                    onClick = { onGameClick("memory_game") }
                )
                GameCard(
                    name = "Balloon Pop",
                    icon = Icons.Default.Celebration,
                    color = Color(0xFFFF4081),
                    modifier = Modifier.weight(1f),
                    onClick = { onGameClick("balloon_pop") }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritingScreen(onBackClick: () -> Unit, speak: (String) -> Unit) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tập Viết: ${characters[currentIndex]}") },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = {
                    IconButton(onClick = { 
                        paths.clear() 
                        showScore = false
                    }) { Icon(Icons.Default.Delete, "Xóa", tint = Color.Red) }
                    IconButton(onClick = { speak(characters[currentIndex]) }) { Icon(Icons.AutoMirrored.Filled.VolumeUp, null) }
                }
            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(24.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp))
                    .onSizeChanged { canvasSize = it }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPath = Path().apply { moveTo(offset.x, offset.y) }
                                showScore = false
                            },
                            onDrag = { change, _ ->
                                currentPath?.lineTo(change.position.x, change.position.y)
                                // Trigger recomposition
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
                Text(
                    text = characters[currentIndex],
                    fontSize = 250.sp,
                    fontWeight = FontWeight.Thin,
                    color = Color.LightGray.copy(alpha = 0.3f)
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    paths.forEach { path ->
                        drawPath(path, color = Color(0xFF2196F3), style = Stroke(width = 20f, cap = StrokeCap.Round))
                    }
                    currentPath?.let { path ->
                        drawPath(path, color = Color(0xFF2196F3), style = Stroke(width = 20f, cap = StrokeCap.Round))
                    }
                }
                
                // Reward Sticker Overlay
                androidx.compose.animation.AnimatedVisibility(
                    visible = showScore && score >= 8.0f,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Text(rewardEmoji, fontSize = 80.sp)
                }
            }

            // Accuracy Feedback
            androidx.compose.animation.AnimatedVisibility(visible = showScore) {
                Text(
                    text = String.format("%.1f/10 Điểm", score),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (score >= 8.0f) Color(0xFF4CAF50) else Color(0xFFFF9800)
                )
            }

            Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                // Check Accuracy Button
                Button(
                    onClick = {
                        if (paths.isNotEmpty() && canvasSize.width > 0) {
                            score = calculateHandwritingScore(
                                char = characters[currentIndex],
                                paths = paths,
                                width = canvasSize.width.toFloat(),
                                height = canvasSize.height.toFloat()
                            )
                            rewardEmoji = rewards.random()
                            showScore = true
                            
                            // Giọng nói phản hồi dựa trên điểm số
                            when {
                                score <= 1.5f -> speak("Bé hãy nhìn kỹ chữ mẫu và viết lại cho đúng nhé!")
                                score < 5.0f -> speak(String.format("Con viết chưa đúng lắm, điểm của con là %.1f. Thử lại nhé!", score))
                                score < 8.0f -> speak(String.format("Con gần đúng rồi, điểm của con là %.1f. Cố gắng lên nào!", score))
                                else -> speak(String.format("Tuyệt vời! Con viết đẹp lắm, được %.1f điểm luôn này!", score))
                            }
                        } else {
                            speak("Con hãy viết gì đó nhé")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.Check, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Kiểm tra")
                }
            }

            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { 
                    if (currentIndex > 0) currentIndex--
                    paths.clear()
                    showScore = false
                    speak(characters[currentIndex])
                }) { Text("Trước") }
                
                Text("${currentIndex + 1} / ${characters.size}", Modifier.padding(horizontal = 24.dp), fontWeight = FontWeight.Bold)

                Button(onClick = { 
                    if (currentIndex < characters.size - 1) currentIndex++
                    paths.clear()
                    showScore = false
                    speak(characters[currentIndex])
                }) { Text("Tiếp") }
            }
        }
    }
}

/**
 * Thuật toán tính điểm viết tay thực tế bằng cách so sánh Pixel
 */
fun calculateHandwritingScore(
    char: String,
    paths: List<androidx.compose.ui.graphics.Path>,
    width: Float,
    height: Float
): Float {
    val size = 128 
    val bitmapTemplate = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val bitmapUser = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    
    val canvasTemplate = AndroidCanvas(bitmapTemplate)
    val canvasUser = AndroidCanvas(bitmapUser)
    
    val paintTemplate = AndroidPaint().apply {
        color = android.graphics.Color.BLACK
        textSize = size * 0.8f
        textAlign = AndroidPaint.Align.CENTER
        typeface = Typeface.DEFAULT // Khớp với font Thin trong UI
        isAntiAlias = true
        // Sử dụng FILL_AND_STROKE để tạo một vùng "đích" đặc, giúp dễ ăn điểm hơn khi đồ đúng
        style = AndroidPaint.Style.FILL_AND_STROKE
        strokeWidth = size * 0.15f // Độ rộng vùng an toàn (hitbox)
    }
    
    val x = size / 2f
    val y = (size / 2f) - ((paintTemplate.descent() + paintTemplate.ascent()) / 2f)
    canvasTemplate.drawText(char, x, y, paintTemplate)
    
    val paintUser = AndroidPaint().apply {
        color = android.graphics.Color.BLACK
        style = AndroidPaint.Style.STROKE
        strokeWidth = size * 0.1f 
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
    
    var intersection = 0
    var templatePixels = 0
    var userPixels = 0
    
    for (i in 0 until size) {
        for (j in 0 until size) {
            val isTemplate = android.graphics.Color.alpha(bitmapTemplate.getPixel(i, j)) > 50
            val isUser = android.graphics.Color.alpha(bitmapUser.getPixel(i, j)) > 50
            
            if (isTemplate) templatePixels++
            if (isUser) userPixels++
            if (isTemplate && isUser) intersection++
        }
    }
    
    if (templatePixels == 0 || userPixels == 0) return 0f
    
    // Coverage: Bé đồ được bao nhiêu phần chữ mẫu
    val coverage = intersection.toFloat() / templatePixels
    // Precision: Bé viết chuẩn bao nhiêu (không lem ra ngoài)
    val precision = intersection.toFloat() / userPixels
    
    // Thuật toán chấm điểm mới: Công bằng nhưng vẫn nhận diện được sai chữ
    // Precision được mũ 1.5 để phạt vừa phải các nét lem, nhưng vẫn đủ để loại chữ sai (như A vs C)
    var finalScore = (coverage * 10f) * (precision * kotlin.math.sqrt(precision.toDouble()).toFloat())

    // Nếu vẽ sai quá nhiều (Precision thấp hơn 40%) thì chắc chắn là viết sai chữ
    if (precision < 0.4f) {
        finalScore = 0f
    }
    
    // Đảm bảo viết đủ ít nhất 25% chữ mới bắt đầu tính điểm
    if (coverage < 0.25f) finalScore = 0f

    finalScore = finalScore.coerceIn(0f, 10f)
    return (kotlin.math.round(finalScore * 10) / 10f)
}

@Composable
fun GameCard(name: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun CategoryCard(category: Category, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = category.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = category.name,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    category: Category,
    onBackClick: () -> Unit,
    onItemClick: (KidioItem) -> Unit,
    onQuizClick: () -> Unit
) {
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
                actions = {
                    Button(
                        onClick = onQuizClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.3f),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(end = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.QuestionMark, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Quiz", fontWeight = FontWeight.Bold)
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(category.color.copy(alpha = 0.05f))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(category.items) { item ->
                    ItemCard(item, onClick = { onItemClick(item) })
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: KidioItem, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.emoji,
                    fontSize = 72.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2D2D2D),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(category: Category, onBackClick: () -> Unit, speak: (String) -> Unit) {
    var currentItem by remember { mutableStateOf(category.items.randomOrNull() ?: KidioItem("Empty", "")) }
    var options by remember { mutableStateOf(generateOptions(category, currentItem)) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var feedbackColor by remember { mutableStateOf(Color.Gray) }
    var score by remember { mutableIntStateOf(0) }
    var questionsAsked by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentItem) {
        if (currentItem.name != "Empty") {
            speak("Bé hãy tìm hình ${currentItem.name} ở đâu nào?")
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
                IconButton(onClick = { speak(currentItem.name) }) {
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
                            speak("Đúng rồi! Bé giỏi quá!")

                            val nextItem = category.items.randomOrNull() ?: currentItem
                            currentItem = nextItem
                            options = generateOptions(category, nextItem)
                        } else {
                            feedback = "Sai rồi, bé thử lại nhé! 💪"
                            feedbackColor = Color(0xFFF44336)
                            speak("Sai rồi, bé thử lại nhé!")
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

@Composable
fun QuizOptionCard(item: KidioItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = item.emoji, fontSize = 80.sp)
        }
    }
}

fun generateOptions(category: Category, correctItem: KidioItem): List<KidioItem> {
    val otherItems = category.items.filter { it != correctItem }.shuffled().take(3)
    return (otherItems + correctItem).shuffled()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryGameScreen(onBackClick: () -> Unit, speak: (String) -> Unit) {
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
                                speak(item.name)

                                if (flippedIndices.size == 2) {
                                    scope.launch {
                                        delay(1000)
                                        val first = flippedIndices.first()
                                        val second = flippedIndices.last()
                                        if (gameItems[first] == gameItems[second]) {
                                            matchedIndices = matchedIndices + first + second
                                            speak("Match found!")
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

@Composable
fun MemoryCard(item: KidioItem, isFlipped: Boolean, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFlipped) Color.White else Color(0xFF673AB7)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                Icon(Icons.Default.QuestionMark, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
            } else {
                Text(
                    text = item.emoji,
                    fontSize = 40.sp,
                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalloonPopScreen(onBackClick: () -> Unit, speak: (String) -> Unit) {
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
                            speak("Pop!")
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

data class Balloon(val id: Int, val x: androidx.compose.ui.unit.Dp, val y: androidx.compose.ui.unit.Dp, val color: Color, val speed: Float)

