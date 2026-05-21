import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/theme/kidio_theme.dart';
import '../models/category.dart';
import '../viewmodels/learning_viewmodel.dart';
import 'learning_detail_view.dart';

class CategoryGridView extends StatelessWidget {
  const CategoryGridView({super.key});

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<LearningViewModel>(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'Choose a Topic',
          style: TextStyle(fontSize: 26, fontWeight: FontWeight.w800),
        ),
      ),
      body: Container(
        decoration: KidioTheme.rainbowGradient(),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: GridView.builder(
            gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 2,
              crossAxisSpacing: 14,
              mainAxisSpacing: 14,
              childAspectRatio: 0.95,
            ),
            itemCount: viewModel.categories.length,
            itemBuilder: (context, index) {
              return CategoryCard(category: viewModel.categories[index]);
            },
          ),
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

    return Material(
      color: Colors.white,
      elevation: 8,
      borderRadius: BorderRadius.circular(28),
      child: InkWell(
        onTap: () {
          viewModel.selectCategory(category);
          Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => const LearningDetailView()),
          );
        },
        borderRadius: BorderRadius.circular(28),
        child: Container(
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(28),
            border: Border.all(color: category.color, width: 4),
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(category.icon, size: 52, color: category.color),
              const SizedBox(height: 10),
              Text(
                category.title,
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.w800,
                  color: category.color,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
