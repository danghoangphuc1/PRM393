import 'package:flutter/material.dart';
import '../models/story.dart';
import '../services/tts_service.dart';

class StoryViewModel extends ChangeNotifier {
  final TtsService _ttsService = TtsService();

  final List<Story> _stories = [
    Story(
      id: 's1',
      title: 'The Brave Little Lion',
      content: 'Once upon a time, in a vast golden savanna, there lived a little lion named Leo. Unlike other lions, Leo was very small, but he had a very big heart. One day, a little bird got stuck in a thick thorn bush...',
      imageUrl: 'https://cdn-icons-png.flaticon.com/512/616/616412.png',
    ),
    Story(
      id: 's2',
      title: 'The Colorful Butterfly',
      content: 'In a garden full of blooming flowers, Bella the Butterfly was sad. She thought her wings were just plain white. She visited the Red Rose, the Blue Bell, and the Yellow Daisy. Each flower shared a bit of its color with her...',
      imageUrl: 'https://cdn-icons-png.flaticon.com/512/3062/3062331.png',
    ),
  ];

  List<Story> get stories => _stories;

  bool _isSpeaking = false;
  bool get isSpeaking => _isSpeaking;

  Future<void> readStory(String text) async {
    _isSpeaking = true;
    notifyListeners();
    await _ttsService.speak(text);
    _isSpeaking = false;
    notifyListeners();
  }

  void stopStory() {
    _ttsService.stop();
    _isSpeaking = false;
    notifyListeners();
  }
}
