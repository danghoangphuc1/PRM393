import 'package:flutter/material.dart';
import '../models/category.dart';
import '../models/learning_item.dart';
import '../services/tts_service.dart';

class LearningViewModel extends ChangeNotifier {
  final TtsService _ttsService = TtsService();

  final List<Category> _categories = [
    Category(id: '1', title: 'Animals', icon: Icons.pets, color: Colors.orange),
    Category(id: '2', title: 'Alphabet', icon: Icons.sort_by_alpha, color: Colors.green),
    Category(id: '3', title: 'Numbers', icon: Icons.looks_one, color: Colors.blue),
    Category(id: '4', title: 'Colors', icon: Icons.color_lens, color: Colors.purple),
  ];

  final Map<String, List<LearningItem>> _itemsByCategory = {
    '1': [
      LearningItem(id: 'a1', categoryId: '1', name: 'Lion', imageUrl: 'https://cdn-icons-png.flaticon.com/512/616/616412.png', audioPath: ''),
      LearningItem(id: 'a2', categoryId: '1', name: 'Elephant', imageUrl: 'https://cdn-icons-png.flaticon.com/512/616/616430.png', audioPath: ''),
      LearningItem(id: 'a3', categoryId: '1', name: 'Monkey', imageUrl: 'https://cdn-icons-png.flaticon.com/512/616/616408.png', audioPath: ''),
    ],
    '3': [
      LearningItem(id: 'n1', categoryId: '3', name: 'One', imageUrl: '', audioPath: ''),
      LearningItem(id: 'n2', categoryId: '3', name: 'Two', imageUrl: '', audioPath: ''),
      LearningItem(id: 'n3', categoryId: '3', name: 'Three', imageUrl: '', audioPath: ''),
    ],
    // Thêm các mục khác sau...
  };

  List<Category> get categories => _categories;

  Category? _selectedCategory;
  Category? get selectedCategory => _selectedCategory;

  List<LearningItem> get itemsForSelectedCategory =>
    _selectedCategory != null ? (_itemsByCategory[_selectedCategory!.id] ?? []) : [];

  void selectCategory(Category category) {
    _selectedCategory = category;
    notifyListeners();
  }

  void speak(String text) {
    _ttsService.speak(text);
  }
}
