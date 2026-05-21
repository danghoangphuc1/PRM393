class LearningItem {
  final String id;
  final String categoryId;
  final String word;
  final String imageUrl;
  final String meaning;
  final String emoji;

  const LearningItem({
    required this.id,
    required this.categoryId,
    required this.word,
    this.imageUrl = '',
    required this.meaning,
    this.emoji = '📚',
  });

  String get displayLabel => word;
}
