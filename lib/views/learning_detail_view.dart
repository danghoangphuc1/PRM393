import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../viewmodels/learning_viewmodel.dart';

class LearningDetailView extends StatelessWidget {
  const LearningDetailView({super.key});

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<LearningViewModel>(context);
    final category = viewModel.selectedCategory;
    final items = viewModel.itemsForSelectedCategory;
    final color = category?.color ?? Colors.orange;

    return Scaffold(
      appBar: AppBar(
        title: Text(
          category?.title ?? 'Words',
          style: const TextStyle(fontSize: 24, fontWeight: FontWeight.w800),
        ),
        backgroundColor: color,
        foregroundColor: Colors.white,
      ),
      body: items.isEmpty
          ? const Center(
              child: Text('Coming soon!', style: TextStyle(fontSize: 22)),
            )
          : Padding(
              padding: const EdgeInsets.all(12),
              child: GridView.builder(
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 2,
                  crossAxisSpacing: 12,
                  mainAxisSpacing: 12,
                  childAspectRatio: 0.72,
                ),
                itemCount: items.length,
                itemBuilder: (context, index) {
                  final item = items[index];
                  return _VocabCard(
                    emoji: item.emoji,
                    word: item.word,
                    meaning: item.meaning,
                    color: color,
                    onTap: () => viewModel.speakItem(item),
                  );
                },
              ),
            ),
    );
  }
}

class _VocabCard extends StatelessWidget {
  final String emoji;
  final String word;
  final String meaning;
  final Color color;
  final VoidCallback onTap;

  const _VocabCard({
    required this.emoji,
    required this.word,
    required this.meaning,
    required this.color,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.white,
      elevation: 4,
      borderRadius: BorderRadius.circular(20),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(20),
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 10),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(emoji, style: const TextStyle(fontSize: 44)),
              const SizedBox(height: 6),
              Text(
                word,
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 4),
              Text(
                meaning,
                textAlign: TextAlign.center,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                style: TextStyle(
                  fontSize: 11,
                  height: 1.2,
                  color: Colors.grey.shade700,
                ),
              ),
              const SizedBox(height: 6),
              Container(
                width: 36,
                height: 36,
                decoration: BoxDecoration(
                  color: color,
                  shape: BoxShape.circle,
                ),
                child: const Icon(
                  Icons.volume_up_rounded,
                  color: Colors.white,
                  size: 20,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
