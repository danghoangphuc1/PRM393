import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class KidioTheme {
  static const Color skyBlue = Color(0xFF4FC3F7);
  static const Color sunnyYellow = Color(0xFFFFD54F);
  static const Color grassGreen = Color(0xFF66BB6A);
  static const Color candyPink = Color(0xFFFF8A80);
  static const Color grapePurple = Color(0xFFBA68C8);
  static const Color cream = Color(0xFFFFF8E1);

  static ThemeData build() {
    final base = ThemeData(
      useMaterial3: true,
      colorScheme: ColorScheme.fromSeed(
        seedColor: const Color(0xFFFF9800),
        brightness: Brightness.light,
        primary: const Color(0xFFFF9800),
        secondary: skyBlue,
        surface: cream,
      ),
      scaffoldBackgroundColor: cream,
      appBarTheme: const AppBarTheme(
        centerTitle: true,
        elevation: 0,
        backgroundColor: Colors.transparent,
        foregroundColor: Color(0xFF5D4037),
      ),
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          minimumSize: const Size(220, 64),
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(32),
          ),
          textStyle: const TextStyle(
            fontSize: 22,
            fontWeight: FontWeight.bold,
          ),
          elevation: 8,
        ),
      ),
      cardTheme: CardThemeData(
        elevation: 6,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(24),
        ),
        color: Colors.white,
      ),
    );

    return base.copyWith(
      textTheme: GoogleFonts.baloo2TextTheme(base.textTheme).apply(
        bodyColor: const Color(0xFF4E342E),
        displayColor: const Color(0xFF4E342E),
      ),
    );
  }

  static BoxDecoration rainbowGradient() {
    return const BoxDecoration(
      gradient: LinearGradient(
        begin: Alignment.topLeft,
        end: Alignment.bottomRight,
        colors: [
          Color(0xFFFFE082),
          Color(0xFFFFAB91),
          Color(0xFF81D4FA),
          Color(0xFFA5D6A7),
        ],
      ),
    );
  }
}
