package com.example.myapplication.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

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

/** Giọng nói: say = bé bấm loa; prompt = lời dẫn/động viên tự động (có thể tắt) */
data class KidioSpeech(
    val say: (String) -> Unit,
    val prompt: (String) -> Unit
)

private val numberEnglishWords = listOf(
    "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
    "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
    "Seventeen", "Eighteen", "Nineteen", "Twenty"
)

private val numberVietnameseWords = listOf(
    "Một", "Hai", "Ba", "Bốn", "Năm", "Sáu", "Bảy", "Tám", "Chín", "Mười",
    "Mười một", "Mười hai", "Mười ba", "Mười bốn", "Mười lăm", "Mười sáu",
    "Mười bảy", "Mười tám", "Mười chín", "Hai mươi"
)

fun numberLearnItems(): List<KidioItem> =
    (1..20).mapIndexed { index, digit ->
        KidioItem(
            name = numberEnglishWords[index],
            emoji = digit.toString(),
            description = numberVietnameseWords[index]
        )
    }

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
        items = numberLearnItems()
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
    ),
    Category(
        id = "months",
        name = "Months",
        icon = Icons.Default.CalendarMonth,
        color = Color(0xFF00BCD4),
        items = listOf(
            KidioItem("January", "❄️", "Tháng Một"),
            KidioItem("February", "💝", "Tháng Hai"),
            KidioItem("March", "🌸", "Tháng Ba"),
            KidioItem("April", "🌧️", "Tháng Tư"),
            KidioItem("May", "🌻", "Tháng Năm"),
            KidioItem("June", "☀️", "Tháng Sáu"),
            KidioItem("July", "🏖️", "Tháng Bảy"),
            KidioItem("August", "🌊", "Tháng Tám"),
            KidioItem("September", "📚", "Tháng Chín"),
            KidioItem("October", "🎃", "Tháng Mười"),
            KidioItem("November", "🍂", "Tháng Mười Một"),
            KidioItem("December", "🎄", "Tháng Mười Hai")
        )
    ),
    Category(
        id = "weekdays",
        name = "Week Days",
        icon = Icons.Default.Event,
        color = Color(0xFF3F51B5),
        items = listOf(
            KidioItem("Monday", "1️⃣", "Thứ Hai"),
            KidioItem("Tuesday", "2️⃣", "Thứ Ba"),
            KidioItem("Wednesday", "3️⃣", "Thứ Tư"),
            KidioItem("Thursday", "4️⃣", "Thứ Năm"),
            KidioItem("Friday", "5️⃣", "Thứ Sáu"),
            KidioItem("Saturday", "6️⃣", "Thứ Bảy"),
            KidioItem("Sunday", "7️⃣", "Chủ Nhật")
        )
    ),
    Category(
        id = "jobs",
        name = "Jobs",
        icon = Icons.Default.Work,
        color = Color(0xFF8BC34A),
        items = listOf(
            KidioItem("Doctor", "👨‍⚕️", "Bác sĩ"),
            KidioItem("Teacher", "👩‍🏫", "Giáo viên"),
            KidioItem("Chef", "👨‍🍳", "Đầu bếp"),
            KidioItem("Police", "👮", "Cảnh sát"),
            KidioItem("Firefighter", "👨‍🚒", "Lính cứu hỏa"),
            KidioItem("Farmer", "👨‍🌾", "Nông dân"),
            KidioItem("Pilot", "👨‍✈️", "Phi công"),
            KidioItem("Artist", "👩‍🎨", "Họa sĩ"),
            KidioItem("Singer", "🎤", "Ca sĩ"),
            KidioItem("Builder", "👷", "Thợ xây")
        )
    )
)

/** Toàn bộ từ vựng từ các chủ đề Let's Learn */
fun allLearnVocabulary(): List<KidioItem> =
    categories.flatMap { it.items }.distinctBy { it.name }

/** Chỉ dùng chữ cái (vd: Ten, không phải 1+0) */
fun KidioItem.spellingWord(): String =
    name.uppercase().filter { it.isLetter() }

fun KidioItem.isDigitVisual(): Boolean = emoji.all { it.isDigit() }

fun KidioItem.displayHint(): String =
    if (description.isNotBlank()) description else name

fun KidioItem.speakLabel(): String = name

/** Từ đủ dài để ghép chữ (2–9 ký tự), bỏ chữ cái đơn lẻ */
fun randomBuildableItem(exclude: KidioItem? = null): KidioItem {
    val pool = allLearnVocabulary().filter { item ->
        val len = item.spellingWord().length
        len in 2..9 && item.name != exclude?.name
    }
    return pool.randomOrNull() ?: allLearnVocabulary().random()
}

fun randomLearnItem(exclude: KidioItem? = null): KidioItem {
    val pool = allLearnVocabulary().filter { it.name != exclude?.name }
    return pool.random()
}

fun listenChooseOptions(correct: KidioItem): List<KidioItem> {
    val others = allLearnVocabulary()
        .filter { it.name != correct.name }
        .shuffled()
        .take(3)
    return (others + correct).shuffled()
}
