import 'package:flutter/foundation.dart';
import 'package:flutter_tts/flutter_tts.dart';

/// Child-friendly English TTS with a female voice when available.
class TtsService {
  final FlutterTts _flutterTts = FlutterTts();
  bool _initialized = false;

  static const double childSpeechRate = 0.38;
  static const double childPitch = 1.15;

  Future<void> _ensureInitialized() async {
    if (_initialized) return;

    await _flutterTts.setLanguage('en-US');
    await _flutterTts.setSpeechRate(childSpeechRate);
    await _flutterTts.setPitch(childPitch);
    await _flutterTts.setVolume(1.0);
    await _flutterTts.awaitSpeakCompletion(true);

    await _selectFemaleVoice();
    _initialized = true;
  }

  Future<void> _selectFemaleVoice() async {
    try {
      final voices = await _flutterTts.getVoices;
      if (voices is! List) return;

      Map<String, String>? best;

      for (final raw in voices) {
        if (raw is! Map) continue;
        final name = '${raw['name']}';
        final locale = '${raw['locale']}'.toLowerCase();
        if (!locale.startsWith('en')) continue;

        final lower = name.toLowerCase();
        final isFemale = lower.contains('female') ||
            lower.contains('woman') ||
            lower.contains('girl') ||
            lower.contains('samantha') ||
            lower.contains('karen') ||
            lower.contains('victoria') ||
            lower.contains('zira') ||
            lower.contains('fiona') ||
            lower.contains('moira') ||
            lower.contains('tessa') ||
            lower.contains('susan');

        if (isFemale) {
          best = {'name': name, 'locale': locale};
          break;
        }
      }

      best ??= _firstEnglishVoice(voices);

      if (best != null) {
        await _flutterTts.setVoice(best);
      }
    } catch (e) {
      debugPrint('TTS voice selection: $e');
    }
  }

  Map<String, String>? _firstEnglishVoice(List voices) {
    for (final raw in voices) {
      if (raw is! Map) continue;
      final locale = '${raw['locale']}'.toLowerCase();
      if (locale.startsWith('en')) {
        return {'name': '${raw['name']}', 'locale': locale};
      }
    }
    return null;
  }

  Future<void> speak(String text) async {
    await _ensureInitialized();
    await _flutterTts.stop();
    await _flutterTts.speak(text);
  }

  Future<void> stop() async {
    await _flutterTts.stop();
  }
}
