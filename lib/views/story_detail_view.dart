import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/story.dart';
import '../viewmodels/story_viewmodel.dart';

class StoryDetailView extends StatefulWidget {
  final Story story;

  const StoryDetailView({super.key, required this.story});

  @override
  State<StoryDetailView> createState() => _StoryDetailViewState();
}

class _StoryDetailViewState extends State<StoryDetailView> {
  @override
  void dispose() {
    // Dừng đọc khi thoát khỏi màn hình
    Provider.of<StoryViewModel>(context, listen: false).stopStory();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<StoryViewModel>(context);

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.story.title),
        backgroundColor: Colors.purple.shade300,
        foregroundColor: Colors.white,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          children: [
            ClipRRect(
              borderRadius: BorderRadius.circular(20),
              child: Image.network(
                widget.story.imageUrl,
                height: 200,
                width: double.infinity,
                fit: BoxFit.contain,
              ),
            ),
            const SizedBox(height: 24),
            Text(
              widget.story.content,
              style: const TextStyle(
                fontSize: 20,
                height: 1.6,
                letterSpacing: 0.5,
              ),
              textAlign: TextAlign.justify,
            ),
            const SizedBox(height: 40),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                FloatingActionButton.large(
                  onPressed: () {
                    if (viewModel.isSpeaking) {
                      viewModel.stopStory();
                    } else {
                      viewModel.readStory(widget.story.content);
                    }
                  },
                  backgroundColor: Colors.purple,
                  child: Icon(
                    viewModel.isSpeaking ? Icons.stop : Icons.play_arrow,
                    size: 40,
                    color: Colors.white,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),
            Text(
              viewModel.isSpeaking ? 'Listening...' : 'Tap to Listen',
              style: const TextStyle(fontSize: 18, color: Colors.purple, fontWeight: FontWeight.bold),
            ),
          ],
        ),
      ),
    );
  }
}
