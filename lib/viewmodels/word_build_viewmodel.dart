import 'dart:math';
import 'package:flutter/material.dart';
import '../data/game_data.dart';

class WordBuildViewModel extends ChangeNotifier {
  final List<WordBuildPuzzle> _puzzles = GameDataRepository.wordBuildPuzzles();
  final Random _random = Random();

  int _index = 0;
  int _score = 0;
  int _streak = 0;
  final List<String?> _slots = [];
  List<String> _pool = [];
  bool _completed = false;
  String? _draggingLetter;

  int get score => _score;
  int get puzzleIndex => _index;
  int get totalPuzzles => _puzzles.length;
  bool get isCompleted => _completed;
  String? get draggingLetter => _draggingLetter;
  List<String?> get slots => List.unmodifiable(_slots);
  List<String> get letterPool => List.unmodifiable(_pool);

  WordBuildPuzzle get currentPuzzle => _puzzles[_index % _puzzles.length];

  WordBuildViewModel() {
    _loadPuzzle();
  }

  void _loadPuzzle() {
    _completed = false;
    final word = currentPuzzle.word;
    _slots
      ..clear()
      ..addAll(List<String?>.filled(word.length, null));
    final letters = word.split('');
    letters.shuffle(_random);
    _pool = letters;
    notifyListeners();
  }

  void setDragging(String? letter) {
    _draggingLetter = letter;
    notifyListeners();
  }

  bool placeLetter(String letter, int slotIndex) {
    if (_completed || slotIndex < 0 || slotIndex >= _slots.length) return false;
    if (_slots[slotIndex] != null) return false;
    final poolIndex = _pool.indexOf(letter);
    if (poolIndex < 0) return false;

    _slots[slotIndex] = letter;
    _pool.removeAt(poolIndex);
    _draggingLetter = null;
    _checkWin();
    notifyListeners();
    return true;
  }

  void removeFromSlot(int slotIndex) {
    if (_completed || slotIndex < 0 || slotIndex >= _slots.length) return;
    final letter = _slots[slotIndex];
    if (letter == null) return;
    _slots[slotIndex] = null;
    _pool.add(letter);
    notifyListeners();
  }

  void _checkWin() {
    if (_slots.any((s) => s == null)) return;
    final built = _slots.join();
    if (built.toUpperCase() == currentPuzzle.word.toUpperCase()) {
      _completed = true;
      _score += 10 + _streak * 2;
      _streak++;
    }
    notifyListeners();
  }

  void nextPuzzle() {
    if (!_completed) return;
    _index = (_index + 1) % _puzzles.length;
    _loadPuzzle();
  }

  void resetCurrent() => _loadPuzzle();
}
