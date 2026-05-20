import 'package:flutter/material.dart';
import '../services/stt_service.dart';

class PronunciationViewModel extends ChangeNotifier {
  final SttService _sttService = SttService();

  String _words = "";
  String get words => _words;

  bool _isListening = false;
  bool get isListening => _isListening;

  String _feedback = "Tap the mic and say the word!";
  String get feedback => _feedback;

  Future<void> startCoach(String targetWord) async {
    bool available = await _sttService.initialize();
    if (available) {
      _isListening = true;
      _words = "";
      _feedback = "Listening...";
      notifyListeners();

      _sttService.startListening((recognizedWords) {
        _words = recognizedWords;
        _checkPronunciation(targetWord);
        notifyListeners();
      });
    } else {
      _feedback = "Speech recognition not available";
      notifyListeners();
    }
  }

  void _checkPronunciation(String target) {
    if (_words.toLowerCase().contains(target.toLowerCase())) {
      _feedback = "Excellent! You said it correctly! 🎉";
    } else if (_words.isNotEmpty) {
      _feedback = "Almost there! Try saying '$target' again.";
    }
  }

  void stopCoach() {
    _sttService.stopListening();
    _isListening = false;
    notifyListeners();
  }
}
