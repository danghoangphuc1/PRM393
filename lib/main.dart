import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:firebase_core/firebase_core.dart';
import 'core/theme/kidio_theme.dart';
import 'viewmodels/home_viewmodel.dart';
import 'viewmodels/learning_viewmodel.dart';
import 'viewmodels/auth_viewmodel.dart';
import 'viewmodels/story_viewmodel.dart';
import 'viewmodels/pronunciation_viewmodel.dart';
import 'views/home_view.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  try {
    await Firebase.initializeApp();
  } catch (e) {
    debugPrint('Firebase initialization failed: $e');
  }

  runApp(const KidioApp());
}

class KidioApp extends StatelessWidget {
  const KidioApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => HomeViewModel()),
        ChangeNotifierProvider(create: (_) => LearningViewModel()),
        ChangeNotifierProvider(create: (_) => AuthViewModel()),
        ChangeNotifierProvider(create: (_) => StoryViewModel()),
        ChangeNotifierProvider(create: (_) => PronunciationViewModel()),
      ],
      child: MaterialApp(
        title: 'KIDIO',
        debugShowCheckedModeBanner: false,
        theme: KidioTheme.build(),
        home: const HomeView(),
      ),
    );
  }
}
