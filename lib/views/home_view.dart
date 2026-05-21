import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/theme/kidio_theme.dart';
import '../core/widgets/kidio_widgets.dart';
import '../viewmodels/auth_viewmodel.dart';
import 'category_grid_view.dart';
import 'games_hub_view.dart';
import 'login_view.dart';
import 'parent_dashboard_view.dart';
import 'pronunciation_coach_view.dart';
import 'story_list_view.dart';
import 'writing_practice_view.dart';

class HomeView extends StatelessWidget {
  const HomeView({super.key});

  @override
  Widget build(BuildContext context) {
    final authViewModel = Provider.of<AuthViewModel>(context);

    return Scaffold(
      body: Container(
        width: double.infinity,
        height: double.infinity,
        decoration: KidioTheme.rainbowGradient(),
        child: SafeArea(
          child: Stack(
            children: [
              Column(
                children: [
                  const SizedBox(height: 12),
                  const Text('🦉', style: TextStyle(fontSize: 56)),
                  const Text(
                    'KIDIO',
                    style: TextStyle(
                      fontSize: 52,
                      fontWeight: FontWeight.w900,
                      color: Colors.white,
                      letterSpacing: 5,
                      shadows: [
                        Shadow(
                          blurRadius: 6,
                          color: Colors.black26,
                          offset: Offset(2, 2),
                        ),
                      ],
                    ),
                  ),
                  const Text(
                    'Learn English & Play!',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.w600,
                      color: Colors.white,
                    ),
                  ),
                  const SizedBox(height: 20),
                  Expanded(
                    child: Center(
                      child: SingleChildScrollView(
                        padding: const EdgeInsets.symmetric(horizontal: 32),
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            KidioMenuButton(
                              title: 'Learn',
                              icon: Icons.menu_book_rounded,
                              color: KidioTheme.grassGreen,
                              onTap: () => Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => const CategoryGridView(),
                                ),
                              ),
                            ),
                            KidioMenuButton(
                              title: 'Games',
                              icon: Icons.videogame_asset_rounded,
                              color: KidioTheme.grapePurple,
                              onTap: () => Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => const GamesHubView(),
                                ),
                              ),
                            ),
                            KidioMenuButton(
                              title: 'Practice',
                              icon: Icons.draw_rounded,
                              color: KidioTheme.skyBlue,
                              onTap: () => Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => const WritingPracticeView(
                                    targetLetter: 'A',
                                  ),
                                ),
                              ),
                            ),
                            KidioMenuButton(
                              title: 'Stories',
                              icon: Icons.auto_stories_rounded,
                              color: KidioTheme.candyPink,
                              onTap: () => Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => const StoryListView(),
                                ),
                              ),
                            ),
                            KidioMenuButton(
                              title: 'Speak',
                              icon: Icons.mic_rounded,
                              color: const Color(0xFFE53935),
                              onTap: () => Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => const PronunciationCoachView(
                                    targetWord: 'Elephant',
                                  ),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                ],
              ),
              Positioned(
                top: 4,
                right: 8,
                child: IconButton(
                  icon: Icon(
                    authViewModel.user != null
                        ? Icons.dashboard_rounded
                        : Icons.settings_rounded,
                    color: Colors.white,
                    size: 30,
                  ),
                  onPressed: () {
                    if (authViewModel.user != null) {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => const ParentDashboardView(),
                        ),
                      );
                    } else {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => const LoginView(),
                        ),
                      );
                    }
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
