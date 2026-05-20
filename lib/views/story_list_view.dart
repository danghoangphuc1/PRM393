import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../viewmodels/story_viewmodel.dart';
import 'story_detail_view.dart';

class StoryListView extends StatelessWidget {
  const StoryListView({super.key});

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<StoryViewModel>(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text('AI Storyteller'),
        backgroundColor: Colors.purple.shade300,
        foregroundColor: Colors.white,
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: viewModel.stories.length,
        itemBuilder: (context, index) {
          final story = viewModel.stories[index];
          return Card(
            margin: const EdgeInsets.only(bottom: 16),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
            elevation: 5,
            child: ListTile(
              contentPadding: const EdgeInsets.all(12),
              leading: ClipRRect(
                borderRadius: BorderRadius.circular(12),
                child: Image.network(story.imageUrl, width: 60, height: 60, fit: BoxFit.cover),
              ),
              title: Text(
                story.title,
                style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              subtitle: Text(
                'By ${story.author}',
                style: TextStyle(color: Colors.grey.shade600),
              ),
              trailing: const Icon(Icons.arrow_forward_ios, color: Colors.purple),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => StoryDetailView(story: story),
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }
}
