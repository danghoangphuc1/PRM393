import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
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
                  onPressed: () => viewModel.clearCanvas(),
                ),
              ],
            ),
            body: Column(
              children: [
                Expanded(
                  child: Center(
                    child: Stack(
                      children: [
                        // Chữ cái mờ làm mẫu bên dưới
                        Center(
                          child: Text(
                            targetLetter,
                            style: TextStyle(
                              fontSize: 300,
                              fontWeight: FontWeight.bold,
                              color: Colors.grey.withOpacity(0.2),
                            ),
                          ),
                        ),
                        // Canvas để bé vẽ lên
                        GestureDetector(
                          onPanUpdate: (details) {
                            RenderBox renderBox = context.findRenderObject() as RenderBox;
                            viewModel.addPoint(renderBox.globalToLocal(details.globalPosition));
                          },
                          onPanEnd: (details) {
                            viewModel.addPoint(null); // Kết thúc một nét vẽ
                            // Ở đây có thể gọi logic chấm điểm
                          },
                          child: CustomPaint(
                            painter: WritingPainter(points: viewModel.points),
                            size: Size.infinite,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.all(24.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      Text(
                        'Score: ${viewModel.score.toStringAsFixed(1)}',
                        style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                      ),
                      ElevatedButton(
                        onPressed: () async {
                          // Giả lập danh sách điểm mục tiêu để test thuật toán
                          viewModel.calculateScore([const Offset(200, 300), const Offset(200, 500)]);

                          // Lưu điểm lên Firestore
                          await viewModel.saveScoreToCloud(targetLetter);

                          if (context.mounted) {
                            showDialog(
                              context: context,
                              builder: (context) => AlertDialog(
                                title: const Text('Great Job!'),
                                content: Text('Your score is ${viewModel.score.toStringAsFixed(1)}/10. Progress saved!'),
                                actions: [
                                  TextButton(
                                    onPressed: () => Navigator.pop(context),
                                    child: const Text('OK'),
                                  ),
                                ],
                              ),
                            );
                          }
                        },
                        child: const Text('Finish'),
                      ),
                    ],
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

class WritingPainter extends CustomPainter {
  final List<Offset?> points;

  WritingPainter({required this.points});

  @override
  void paint(Canvas canvas, Size size) {
    Paint paint = Paint()
      ..color = Colors.blue
      ..strokeCap = StrokeCap.round
      ..strokeWidth = 10.0;

    for (int i = 0; i < points.length - 1; i++) {
      if (points[i] != null && points[i + 1] != null) {
        canvas.drawLine(points[i]!, points[i + 1]!, paint);
      }
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => true;
}
