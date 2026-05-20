class Story {
  final String id;
  final String title;
  final String content;
  final String imageUrl;
  final String author;

  Story({
    required this.id,
    required this.title,
    required this.content,
    required this.imageUrl,
    this.author = "AI Storyteller",
  });
}
