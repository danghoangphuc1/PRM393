import '../models/learning_item.dart';
import 'vocabulary_repository.dart';
import 'word_emoji_map.dart';

class WordBuildPuzzle {
  final String word;
  final String emoji;
  final String hint;
  final int level;

  const WordBuildPuzzle({
    required this.word,
    required this.emoji,
    required this.hint,
    required this.level,
  });
}

class FillBlankQuestion {
  final String sentenceBefore;
  final String sentenceAfter;
  final String correctWord;
  final List<String> choices;
  final String emoji;

  const FillBlankQuestion({
    required this.sentenceBefore,
    required this.sentenceAfter,
    required this.correctWord,
    required this.choices,
    this.emoji = '✏️',
  });

  String get fullSentence => '$sentenceBefore $correctWord $sentenceAfter';
}

class GameDataRepository {
  static List<WordBuildPuzzle> wordBuildPuzzles() {
    const easy = ['CAT', 'DOG', 'SUN', 'BUS', 'HAT', 'CUP', 'BED', 'EGG'];
    const medium = [
      'APPLE', 'BREAD', 'CHAIR', 'GREEN', 'HAPPY', 'MOUSE', 'PIZZA', 'TRAIN',
    ];
    const hard = [
      'ELEPHANT', 'BUTTERFLY', 'RAINBOW', 'SANDWICH', 'UMBRELLA', 'CHOCOLATE',
    ];

    final puzzles = <WordBuildPuzzle>[];
    var level = 1;

    for (final group in [easy, medium, hard]) {
      for (final w in group) {
        puzzles.add(WordBuildPuzzle(
          word: w,
          emoji: WordEmojiMap.emoji(w),
          hint: WordEmojiMap.hint(w),
          level: level,
        ));
        level++;
      }
    }
    return puzzles;
  }

  static List<FillBlankQuestion> fillBlankQuestions() {
    return const [
      FillBlankQuestion(
        sentenceBefore: 'I',
        sentenceAfter: 'an apple.',
        correctWord: 'eat',
        choices: ['eat', 'sleep', 'drive', 'paint'],
        emoji: '🍎',
      ),
      FillBlankQuestion(
        sentenceBefore: 'The cat is',
        sentenceAfter: 'the tree.',
        correctWord: 'on',
        choices: ['on', 'under', 'blue', 'loud'],
        emoji: '🐱',
      ),
      FillBlankQuestion(
        sentenceBefore: 'We go to',
        sentenceAfter: 'by bus.',
        correctWord: 'school',
        choices: ['school', 'ocean', 'pizza', 'moon'],
        emoji: '🏫',
      ),
      FillBlankQuestion(
        sentenceBefore: 'The sky is',
        sentenceAfter: 'today.',
        correctWord: 'blue',
        choices: ['blue', 'square', 'hungry', 'run'],
        emoji: '☀️',
      ),
      FillBlankQuestion(
        sentenceBefore: 'I brush my',
        sentenceAfter: 'every morning.',
        correctWord: 'teeth',
        choices: ['teeth', 'shoes', 'clouds', 'trains'],
        emoji: '🪥',
      ),
      FillBlankQuestion(
        sentenceBefore: 'My',
        sentenceAfter: 'is very kind.',
        correctWord: 'mother',
        choices: ['mother', 'bicycle', 'yellow', 'jump'],
        emoji: '👩',
      ),
      FillBlankQuestion(
        sentenceBefore: 'The dog can',
        sentenceAfter: 'fast.',
        correctWord: 'run',
        choices: ['run', 'read', 'cook', 'sleep'],
        emoji: '🐶',
      ),
      FillBlankQuestion(
        sentenceBefore: 'I drink',
        sentenceAfter: 'with breakfast.',
        correctWord: 'milk',
        choices: ['milk', 'chair', 'happy', 'train'],
        emoji: '🥛',
      ),
      FillBlankQuestion(
        sentenceBefore: 'Birds can',
        sentenceAfter: 'in the sky.',
        correctWord: 'fly',
        choices: ['fly', 'swim', 'eat', 'draw'],
        emoji: '🐦',
      ),
      FillBlankQuestion(
        sentenceBefore: 'It is',
        sentenceAfter: 'outside. I need a coat.',
        correctWord: 'cold',
        choices: ['cold', 'round', 'sweet', 'tall'],
        emoji: '❄️',
      ),
      FillBlankQuestion(
        sentenceBefore: 'I play with my',
        sentenceAfter: 'in the park.',
        correctWord: 'friends',
        choices: ['friends', 'clouds', 'bread', 'purple'],
        emoji: '🛝',
      ),
      FillBlankQuestion(
        sentenceBefore: 'The',
        sentenceAfter: 'is red and round.',
        correctWord: 'apple',
        choices: ['apple', 'bus', 'run', 'happy'],
        emoji: '🍎',
      ),
    ];
  }

  static List<ListenChooseRound> listenChooseRounds() {
    final items = VocabularyRepository.allItems();
    items.shuffle();
    final picked = items.take(24).toList();

    return picked.map((item) {
      final distractors = VocabularyRepository.getItems(item.categoryId)
          .where((e) => e.id != item.id)
          .toList()
        ..shuffle();
      final options = [item, ...distractors.take(3)]..shuffle();
      return ListenChooseRound(correct: item, options: options);
    }).toList();
  }
}

class ListenChooseRound {
  final LearningItem correct;
  final List<LearningItem> options;

  const ListenChooseRound({required this.correct, required this.options});
}
