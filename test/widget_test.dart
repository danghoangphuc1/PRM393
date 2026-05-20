import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:provider/provider.dart';
import 'package:kidio_app/main.dart';
import 'package:kidio_app/viewmodels/home_viewmodel.dart';
import 'package:kidio_app/viewmodels/learning_viewmodel.dart';
import 'package:kidio_app/viewmodels/auth_viewmodel.dart';
import 'package:kidio_app/viewmodels/story_viewmodel.dart';
import 'package:kidio_app/viewmodels/pronunciation_viewmodel.dart';
import 'package:kidio_app/views/home_view.dart';

void main() {
  testWidgets('HomeView has main menu buttons test', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    // Note: We wrap in MultiProvider because HomeView depends on AuthViewModel
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

    // Đợi một chút để UI render xong
    await tester.pumpAndSettle();

    // Kiểm tra xem logo KIDIO có tồn tại không
    expect(find.text('KIDIO'), findsOneWidget);

    // Kiểm tra các nút chức năng chính
    expect(find.text('LEARN'), findsOneWidget);
    expect(find.text('PRACTICE'), findsOneWidget);
    expect(find.text('STORIES'), findsOneWidget);
    expect(find.text('SPEAK'), findsOneWidget);

    // Kiểm tra sự tồn tại của icon menu
    expect(find.byIcon(Icons.menu_book), findsOneWidget);
    expect(find.byIcon(Icons.edit), findsOneWidget);
  });
}
