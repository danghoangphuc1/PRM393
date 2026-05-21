/// Emoji hints for word-building puzzles (child uses picture to guess the word).
class WordEmojiMap {
  static const Map<String, String> emojiByWord = {
    'CAT': '🐱',
    'DOG': '🐶',
    'SUN': '☀️',
    'BUS': '🚌',
    'HAT': '🎩',
    'CUP': '☕',
    'BED': '🛏️',
    'EGG': '🥚',
    'APPLE': '🍎',
    'BREAD': '🍞',
    'CHAIR': '🪑',
    'GREEN': '🟢',
    'HAPPY': '😊',
    'MOUSE': '🐭',
    'PIZZA': '🍕',
    'TRAIN': '🚆',
    'ELEPHANT': '🐘',
    'BUTTERFLY': '🦋',
    'RAINBOW': '🌈',
    'SANDWICH': '🥪',
    'UMBRELLA': '☂️',
    'CHOCOLATE': '🍫',
  };

  static const Map<String, String> hintByWord = {
    'CAT': 'A furry pet that says meow',
    'DOG': 'A friendly pet that barks',
    'SUN': 'Bright and warm in the sky',
    'BUS': 'A big vehicle for many people',
    'HAT': 'You wear it on your head',
    'CUP': 'You drink from it',
    'BED': 'You sleep on it',
    'EGG': 'Comes from a chicken',
    'APPLE': 'A red or green fruit',
    'BREAD': 'Soft food from the bakery',
    'CHAIR': 'You sit on it',
    'GREEN': 'The color of grass',
    'HAPPY': 'When you feel joyful',
    'MOUSE': 'A small animal or computer tool',
    'PIZZA': 'A yummy Italian food',
    'TRAIN': 'Runs on tracks',
    'ELEPHANT': 'A huge animal with a trunk',
    'BUTTERFLY': 'A colorful flying insect',
    'RAINBOW': 'Colors in the sky after rain',
    'SANDWICH': 'Food between two slices of bread',
    'UMBRELLA': 'Keeps you dry in the rain',
    'CHOCOLATE': 'A sweet brown treat',
  };

  static String emoji(String word) =>
      emojiByWord[word.toUpperCase()] ?? '❓';

  static String hint(String word) =>
      hintByWord[word.toUpperCase()] ?? 'Look at the picture!';
}
