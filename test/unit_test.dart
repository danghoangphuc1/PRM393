import 'package:flutter_test/flutter_test.dart';
import 'package:kidio_app/viewmodels/writing_viewmodel.dart';
import 'package:flutter/material.dart';

void main() {
  group('WritingViewModel Scoring Logic Tests', () {
    late WritingViewModel viewModel;

    setUp(() {
      viewModel = WritingViewModel();
    });

    test('Score should be 10 when points perfectly match target path', () {
      // Setup target path
      final targetPath = [
        const Offset(100, 100),
        const Offset(100, 200),
      ];

      // Add points that match exactly
      viewModel.addPoint(const Offset(100, 100));
      viewModel.addPoint(const Offset(100, 200));

      // Calculate score
      viewModel.calculateScore(targetPath);

      // Score should be 10 (perfect)
      expect(viewModel.score, 10.0);
    });

    test('Score should be lower when points are far from target path', () {
      final targetPath = [
        const Offset(100, 100),
      ];

      // Add a point far away (100 pixels away)
      // Our formula: (10 - (averageDistance / 10)).clamp(0, 10)
      // distance = 100, score = (10 - (100 / 10)) = 0
      viewModel.addPoint(const Offset(200, 100));

      viewModel.calculateScore(targetPath);

      expect(viewModel.score, 0.0);
    });

    test('Score should be 0 when no points are drawn', () {
      final targetPath = [const Offset(100, 100)];

      viewModel.calculateScore(targetPath);

      expect(viewModel.score, 0.0);
    });

    test('Clear canvas should reset score and points', () {
      viewModel.addPoint(const Offset(50, 50));
      viewModel.clearCanvas();

      expect(viewModel.points.isEmpty, true);
      expect(viewModel.score, 0.0);
    });
  });
}
