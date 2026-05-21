import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../core/theme/kidio_theme.dart';
import '../viewmodels/writing_viewmodel.dart';

class WritingPracticeView extends StatelessWidget {
  final String targetLetter;

  const WritingPracticeView({super.key, required this.targetLetter});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => WritingViewModel(),
      child: Consumer<WritingViewModel>(
        builder: (context, viewModel, child) {
          return Scaffold(
            appBar: AppBar(
              title: Text('Practice Writing: $targetLetter'),
              actions: [
                IconButton(
                  icon: const Icon(Icons.refresh),
                  onPressed: viewModel.clearCanvas,
                ),
              ],
            ),
            body: Column(
              children: [
                Expanded(
                  child: LayoutBuilder(
                    builder: (context, constraints) {
                      final canvasSize = Size(
                        constraints.maxWidth,
                        constraints.maxHeight,
                      );
                      viewModel.setCanvasSize(canvasSize);
                      return Stack(
                        children: [
                          Center(
                            child: Text(
                              targetLetter,
                              style: TextStyle(
                                fontSize: 300,
                                fontWeight: FontWeight.bold,
                                color: Colors.grey.withValues(alpha: 0.2),
                              ),
                            ),
                          ),
                          GestureDetector(
                            onPanUpdate: (details) {
                              final box = context.findRenderObject() as RenderBox;
                              viewModel.addPoint(
                                box.globalToLocal(details.globalPosition),
                              );
                            },
                            onPanEnd: (_) => viewModel.addPoint(null),
                            child: CustomPaint(
                              painter: WritingPainter(points: viewModel.points),
                              size: canvasSize,
                            ),
                          ),
                          Positioned(
                            left: 12,
                            bottom: 12,
                            child: _ScoreChip(score: viewModel.score),
                          ),
                        ],
                      );
                    },
                  ),
                ),
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.fromLTRB(20, 16, 20, 24),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: const BorderRadius.vertical(
                      top: Radius.circular(28),
                    ),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withValues(alpha: 0.08),
                        blurRadius: 12,
                        offset: const Offset(0, -4),
                      ),
                    ],
                  ),
                  child: LayoutBuilder(
                    builder: (context, _) {
                      return Row(
                        children: [
                          Expanded(
                            child: Text(
                              viewModel.score > 0
                                  ? 'Score: ${viewModel.score.toStringAsFixed(1)} / 10'
                                  : 'Trace the letter!',
                              style: const TextStyle(
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                          ElevatedButton(
                            onPressed: () async {
                              viewModel.calculateScore(
                                letter: targetLetter,
                                canvasSize: viewModel.canvasSize,
                              );
                              await viewModel.saveScoreToCloud(targetLetter);

                              if (!context.mounted) return;
                              final stars = viewModel.score >= 8
                                  ? '🌟🌟🌟'
                                  : viewModel.score >= 5
                                      ? '🌟🌟'
                                      : viewModel.score >= 3
                                          ? '🌟'
                                          : '💪';
                              showDialog(
                                context: context,
                                builder: (ctx) => AlertDialog(
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(24),
                                  ),
                                  title: Text(
                                    viewModel.score >= 5
                                        ? 'Great Job! $stars'
                                        : 'Keep Trying! $stars',
                                  ),
                                  content: Text(
                                    'Your score is ${viewModel.score.toStringAsFixed(1)} / 10.\n'
                                    'Trace slowly on the gray letter.',
                                  ),
                                  actions: [
                                    TextButton(
                                      onPressed: () => Navigator.pop(ctx),
                                      child: const Text('OK'),
                                    ),
                                  ],
                                ),
                              );
                            },
                            style: ElevatedButton.styleFrom(
                              backgroundColor: KidioTheme.grassGreen,
                              foregroundColor: Colors.white,
                              minimumSize: const Size(120, 52),
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(26),
                              ),
                            ),
                            child: const Text('Finish'),
                          ),
                        ],
                      );
                    },
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}

class _ScoreChip extends StatelessWidget {
  final double score;

  const _ScoreChip({required this.score});

  @override
  Widget build(BuildContext context) {
    if (score <= 0) return const SizedBox.shrink();
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
      decoration: BoxDecoration(
        color: KidioTheme.grassGreen,
        borderRadius: BorderRadius.circular(20),
      ),
      child: Text(
        '${score.toStringAsFixed(1)} / 10',
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
          fontSize: 16,
        ),
      ),
    );
  }
}

class WritingPainter extends CustomPainter {
  final List<Offset?> points;

  WritingPainter({required this.points});

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = Colors.blue
      ..strokeCap = StrokeCap.round
      ..strokeWidth = 12.0;

    for (var i = 0; i < points.length - 1; i++) {
      if (points[i] != null && points[i + 1] != null) {
        canvas.drawLine(points[i]!, points[i + 1]!, paint);
      }
    }
  }

  @override
  bool shouldRepaint(covariant WritingPainter oldDelegate) =>
      oldDelegate.points != points;
}
