import 'package:flutter/material.dart';
import '../data/game_data.dart';

class FillBlankViewModel extends ChangeNotifier {
  final List<FillBlankQuestion> _questions =
      GameDataRepository.fillBlankQuestions();

  int _index = 0;
  int _score = 0;
  int _stars = 0;
  String? _selected;
  bool? _lastCorrect;

  int get score => _score;
  int get stars => _stars;
  int get questionIndex => _index;
  int get totalQuestions => _questions.length;
  String? get selected => _selected;
  bool? get lastCorrect => _lastCorrect;
  bool get answered => _lastCorrect != null;

  FillBlankQuestion get current => _questions[_index % _questions.length];

  void selectAnswer(String word) {
    if (answered) return;
    _selected = word;
    final correct = word == current.correctWord;
    _lastCorrect = correct;
    if (correct) {
      _score += 15;
      _stars = (_stars + 1).clamp(0, 3);
    } else {
      _stars = 0;
    }
    notifyListeners();
  }

  void nextQuestion() {
    if (!answered) return;
    _index = (_index + 1) % _questions.length;
    _selected = null;
    _lastCorrect = null;
    notifyListeners();
  }

  void retry() {
    _selected = null;
    _lastCorrect = null;
    notifyListeners();
  }
}
