import 'package:flutter/material.dart';
import '../services/firestore_service.dart';

class WritingViewModel extends ChangeNotifier {
  final FirestoreService _firestoreService = FirestoreService();

  List<Offset?> _points = [];
  List<Offset?> get points => _points;

  double _score = 0;
  double get score => _score;

  void addPoint(Offset? point) {
    _points.add(point);
    notifyListeners();
  }

  void clearCanvas() {
    _points.clear();
    _score = 0;
    notifyListeners();
  }

  Future<void> saveScoreToCloud(String letter) async {
    if (_score > 0) {
      await _firestoreService.saveWritingScore(letter, _score);
    }
  }

  void calculateScore(List<Offset> targetPath) {
    if (_points.isEmpty) {
      _score = 0;
      notifyListeners();
      return;
    }

    // Logic chấm điểm cơ bản: tính khoảng cách trung bình từ các điểm vẽ đến điểm gần nhất trên đường mẫu
    double totalDistance = 0;
    int validPoints = 0;

    for (var point in _points) {
      if (point == null) continue;

      double minDistance = double.infinity;
      for (var target in targetPath) {
        double dist = (point - target).distance;
        if (dist < minDistance) minDistance = dist;
      }
      totalDistance += minDistance;
      validPoints++;
    }

    if (validPoints == 0) {
      _score = 0;
    } else {
      double averageDistance = totalDistance / validPoints;
      // Chuyển đổi khoảng cách thành thang điểm 1-10 (khoảng cách càng nhỏ điểm càng cao)
      _score = (10 - (averageDistance / 10)).clamp(0, 10);
    }

    notifyListeners();
  }
}
