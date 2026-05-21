import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:provider/provider.dart';
import 'package:kidio_app/viewmodels/home_viewmodel.dart';
import 'package:kidio_app/viewmodels/learning_viewmodel.dart';
import 'package:kidio_app/viewmodels/auth_viewmodel.dart';
import 'package:kidio_app/viewmodels/story_viewmodel.dart';
import 'package:kidio_app/viewmodels/pronunciation_viewmodel.dart';
import 'package:kidio_app/views/home_view.dart';

void main() {
  testWidgets('HomeView shows main menu', (WidgetTester tester) async {
    await tester.pumpWidget(
      MultiProvider(
        providers: [
          ChangeNotifierProvider(create: (_) => HomeViewModel()),
          ChangeNotifierProvider(create: (_) => LearningViewModel()),
          ChangeNotifierProvider(create: (_) => AuthViewModel()),
          ChangeNotifierProvider(create: (_) => StoryViewModel()),
          ChangeNotifierProvider(create: (_) => PronunciationViewModel()),
        ],
        child: const MaterialApp(
          home: HomeView(),
        ),
      ),
    );

    await tester.pumpAndSettle();

    expect(find.text('KIDIO'), findsOneWidget);
    expect(find.text('Learn'), findsOneWidget);
    expect(find.text('Games'), findsOneWidget);
    expect(find.text('Practice'), findsOneWidget);
    expect(find.text('Stories'), findsOneWidget);
    expect(find.text('Speak'), findsOneWidget);
  });
}
