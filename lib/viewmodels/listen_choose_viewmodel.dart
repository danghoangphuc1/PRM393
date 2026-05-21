import 'package:flutter/material.dart';
import '../data/game_data.dart';
import '../models/learning_item.dart';
import '../services/tts_service.dart';

class ListenChooseViewModel extends ChangeNotifier {
  final TtsService _tts = TtsService();
  final List<ListenChooseRound> _rounds =
      GameDataRepository.listenChooseRounds();

  int _index = 0;
  int _score = 0;
  int _correctCount = 0;
  String? _selectedId;
  bool? _lastCorrect;
  bool _played = false;

  int get score => _score;
  int get correctCount => _correctCount;
  int get roundIndex => _index;
  int get totalRounds => _rounds.length;
  double get progress => (_index + 1) / totalRounds;
  String? get selectedId => _selectedId;
  bool? get lastCorrect => _lastCorrect;
  bool get answered => _lastCorrect != null;
  bool get played => _played;

  ListenChooseRound get current => _rounds[_index % _rounds.length];

  Future<void> playWord() async {
    _played = true;
    await _tts.speak(current.correct.word);
    notifyListeners();
  }

  void selectOption(LearningItem item) {
    if (answered) return;
    _selectedId = item.id;
    final correct = item.id == current.correct.id;
    _lastCorrect = correct;
    if (correct) {
      _score += 20;
      _correctCount++;
    }
    notifyListeners();
  }

  void nextRound() {
    if (!answered) return;
    _index = (_index + 1) % _rounds.length;
    _selectedId = null;
    _lastCorrect = null;
    _played = false;
    notifyListeners();
  }

  void retryRound() {
    _selectedId = null;
    _lastCorrect = null;
    _played = false;
    notifyListeners();
  }
}
