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

    return Scaffold(
      appBar: AppBar(
        title: Text(category?.title ?? 'Detail'),
        backgroundColor: category?.color ?? Colors.orange,
        foregroundColor: Colors.white,
      ),
      body: items.isEmpty
          ? const Center(child: Text('Coming soon!'))
          : Padding(
              padding: const EdgeInsets.all(16.0),
              child: GridView.builder(
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 2,
                  crossAxisSpacing: 16,
                  mainAxisSpacing: 16,
                  childAspectRatio: 0.85,
                ),
                itemCount: items.length,
                itemBuilder: (context, index) {
                  final item = items[index];
                  return GestureDetector(
                    onTap: () {
                      viewModel.speak(item.name);
                    },
                    child: Card(
                      elevation: 4,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20),
                      ),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Expanded(
                            child: Padding(
                              padding: const EdgeInsets.all(12.0),
                              child: item.imageUrl.isNotEmpty
                                  ? Image.network(item.imageUrl, fit: BoxFit.contain)
                                  : Icon(Icons.help_outline, size: 80, color: category?.color),
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.only(bottom: 12.0),
                            child: Text(
                              item.name,
                              style: const TextStyle(
                                fontSize: 24,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                          Container(
                            margin: const EdgeInsets.only(bottom: 8),
                            padding: const EdgeInsets.all(4),
                            decoration: BoxDecoration(
                              color: category?.color,
                              shape: BoxShape.circle,
                            ),
                            child: const Icon(Icons.volume_up, color: Colors.white, size: 20),
                          )
                        ],
                      ),
                    ),
                  );
                },
              ),
            ),
    );
  }
}
