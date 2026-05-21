import 'package:flutter/material.dart';
import '../data/vocabulary_repository.dart';
import '../models/category.dart';
import '../models/learning_item.dart';
import '../services/tts_service.dart';

class LearningViewModel extends ChangeNotifier {
  final TtsService _ttsService = TtsService();

  List<Category> get categories => VocabularyRepository.categories;

  Category? _selectedCategory;
  Category? get selectedCategory => _selectedCategory;

  List<LearningItem> get itemsForSelectedCategory =>
      _selectedCategory != null
          ? VocabularyRepository.getItems(_selectedCategory!.id)
          : [];

  void selectCategory(Category category) {
    _selectedCategory = category;
    notifyListeners();
  }

  Future<void> speak(String text) => _ttsService.speak(text);

  Future<void> speakItem(LearningItem item) => _ttsService.speak(item.word);
}
