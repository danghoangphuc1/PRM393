import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'category_grid_view.dart';
import 'writing_practice_view.dart';
import 'login_view.dart';
import 'parent_dashboard_view.dart';
import 'story_list_view.dart';
import 'pronunciation_coach_view.dart';
import '../viewmodels/auth_viewmodel.dart';

class HomeView extends StatelessWidget {
  const HomeView({super.key});

  @override
  Widget build(BuildContext context) {
    final authViewModel = Provider.of<AuthViewModel>(context);

    return Scaffold(
      body: Container(
        width: double.infinity,
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Colors.orange.shade300, Colors.orange.shade600],
          ),
        ),
        child: Stack(
          children: [
            Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Text(
                  'KIDIO',
                  style: TextStyle(
                    fontSize: 80,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                    letterSpacing: 10,
                  ),
                ),
                const SizedBox(height: 50),
                _MenuButton(
                  title: 'LEARN',
                  icon: Icons.menu_book,
                  color: Colors.green,
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const CategoryGridView()),
                    );
                  },
                ),
                const SizedBox(height: 20),
                _MenuButton(
                  title: 'PRACTICE',
                  icon: Icons.edit,
                  color: Colors.blue,
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const WritingPracticeView(targetLetter: 'A')),
                    );
                  },
                ),
                const SizedBox(height: 20),
                _MenuButton(
                  title: 'STORIES',
                  icon: Icons.auto_stories,
                  color: Colors.purple,
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const StoryListView()),
                    );
                  },
                ),
                const SizedBox(height: 20),
                _MenuButton(
                  title: 'SPEAK',
                  icon: Icons.mic,
                  color: Colors.redAccent,
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const PronunciationCoachView(targetWord: 'Elephant')),
                    );
                  },
                ),
              ],
            ),
            Positioned(
              top: 40,
              right: 20,
              child: IconButton(
                icon: Icon(
                  authViewModel.user != null ? Icons.dashboard : Icons.settings,
                  color: Colors.white,
                  size: 30,
                ),
                onPressed: () {
                  if (authViewModel.user != null) {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const ParentDashboardView()),
                    );
                  } else {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const LoginView()),
                    );
                  }
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _MenuButton extends StatelessWidget {
  final String title;
  final IconData icon;
  final Color color;
  final VoidCallback onTap;

  const _MenuButton({
    required this.title,
    required this.icon,
    required this.color,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return ElevatedButton.icon(
      onPressed: onTap,
      icon: Icon(icon, size: 32),
      label: Text(
        title,
        style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
      ),
      style: ElevatedButton.styleFrom(
        foregroundColor: color,
        backgroundColor: Colors.white,
        minimumSize: const Size(250, 70),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(35),
        ),
        elevation: 10,
      ),
    );
  }
}
