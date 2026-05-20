import 'package:flutter_tts/flutter_tts.dart';

class TtsService {
  final FlutterTts _flutterTts = FlutterTts();

  Future<void> speak(String text) async {
    await _flutterTts.setLanguage("en-US");
    await _flutterTts.setPitch(1.0);
    await _flutterTts.setSpeechRate(0.5); // Tốc độ chậm cho bé dễ nghe
    await _flutterTts.speak(text);
  }

  void stop() async {
    await _flutterTts.stop();
  }
}
