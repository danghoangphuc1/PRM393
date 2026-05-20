import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../viewmodels/learning_viewmodel.dart';
import '../models/category.dart';
import 'learning_detail_view.dart';

class CategoryGridView extends StatelessWidget {
  const CategoryGridView({super.key});

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<LearningViewModel>(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text('KIDIO Learning'),
        centerTitle: true,
        backgroundColor: Colors.transparent,
        elevation: 0,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: GridView.builder(
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
            crossAxisSpacing: 16,
            mainAxisSpacing: 16,
            childAspectRatio: 1,
          ),
          itemCount: viewModel.categories.length,
          itemBuilder: (context, index) {
            final category = viewModel.categories[index];
            return CategoryCard(category: category);
          },
        ),
      ),
    );
  }
}

class CategoryCard extends StatelessWidget {
  final Category category;

  const CategoryCard({super.key, required this.category});

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<LearningViewModel>(context, listen: false);

    return InkWell(
      onTap: () {
        viewModel.selectCategory(category);
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const LearningDetailView()),
        );
      },
      child: Container(
        decoration: BoxDecoration(
          color: category.color.withOpacity(0.2),
          borderRadius: BorderRadius.circular(24),
          border: Border.all(color: category.color, width: 3),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(category.icon, size: 60, color: category.color),
            const SizedBox(height: 12),
            Text(
              category.title,
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
                color: category.color.withOpacity(0.9),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
