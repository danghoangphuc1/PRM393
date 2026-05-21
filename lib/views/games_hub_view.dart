import 'package:flutter/material.dart';
import '../core/theme/kidio_theme.dart';
import '../core/widgets/kidio_widgets.dart';
import 'fill_blank_game_view.dart';
import 'listen_choose_game_view.dart';
import 'word_build_game_view.dart';

class GamesHubView extends StatelessWidget {
  const GamesHubView({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      appBar: AppBar(
        title: const Text(
          'Fun Games',
          style: TextStyle(fontSize: 28, fontWeight: FontWeight.w800),
        ),
      ),
      body: Container(
        decoration: KidioTheme.rainbowGradient(),
        child: SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(20),
            child: Column(
              children: [
                const Text(
                  'Pick a game!',
                  style: TextStyle(
                    fontSize: 26,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                  ),
                ),
                const SizedBox(height: 24),
                KidioMenuButton(
                  title: 'Build Words',
                  icon: Icons.extension,
                  color: KidioTheme.grassGreen,
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => const WordBuildGameView(),
                    ),
                  ),
                ),
                const SizedBox(height: 16),
                KidioMenuButton(
                  title: 'Fill the Blank',
                  icon: Icons.spellcheck,
                  color: KidioTheme.skyBlue,
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => const FillBlankGameView(),
                    ),
                  ),
                ),
                const SizedBox(height: 16),
                KidioMenuButton(
                  title: 'Listen & Choose',
                  icon: Icons.hearing,
                  color: KidioTheme.candyPink,
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => const ListenChooseGameView(),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
