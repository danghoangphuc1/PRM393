import 'package:flutter/material.dart';
import '../services/firestore_service.dart';

class WritingViewModel extends ChangeNotifier {
  final FirestoreService _firestoreService = FirestoreService();

  final List<Offset?> _points = [];
  List<Offset?> get points => _points;

  double _score = 0;
  double get score => _score;

  Size _canvasSize = Size.zero;
  Size get canvasSize => _canvasSize;
  void setCanvasSize(Size size) => _canvasSize = size;

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

  /// Child-friendly scoring: compares strokes to the letter area on canvas.
  void calculateScore({
    required String letter,
    required Size canvasSize,
  }) {
    final drawn = _points.whereType<Offset>().toList();
    if (drawn.length < 8) {
      _score = 0;
      notifyListeners();
      return;
    }

    final targets = _sampleLetterTargets(letter, canvasSize);
    if (targets.isEmpty) {
      _score = 0;
      notifyListeners();
      return;
    }

    final letterBounds = _letterBounds(letter, canvasSize);
    final threshold = (letterBounds.width * 0.14).clamp(28.0, 55.0);

    var matched = 0;
    var totalDistance = 0.0;

    for (final point in drawn) {
      var minDist = double.infinity;
      for (final target in targets) {
        final d = (point - target).distance;
        if (d < minDist) minDist = d;
      }
      totalDistance += minDist;
      if (minDist <= threshold) matched++;
    }

    final matchRatio = matched / drawn.length;
    final avgDistance = totalDistance / drawn.length;

    final drawnBounds = _boundsOf(drawn);
    final overlap = _overlapRatio(drawnBounds, letterBounds);

    // Mostly outside the letter area → wrong shape / wrong place
    if (overlap < 0.15) {
      _score = (matchRatio * 3).clamp(0, 3);
      notifyListeners();
      return;
    }

    // Generous bands for young children
    double base;
    if (matchRatio >= 0.45 && avgDistance <= threshold * 1.8) {
      base = 8.5 + (matchRatio - 0.45) * 4;
    } else if (matchRatio >= 0.28 && avgDistance <= threshold * 2.5) {
      base = 6 + (matchRatio - 0.28) * 8;
    } else if (matchRatio >= 0.15) {
      base = 3 + matchRatio * 10;
    } else {
      base = matchRatio * 8;
    }

    base += overlap.clamp(0, 1) * 1.2;
    _score = base.clamp(0, 10);
    notifyListeners();
  }

  static List<Offset> _sampleLetterTargets(String letter, Size canvasSize) {
    const fontSize = 300.0;
    final style = const TextStyle(
      fontSize: fontSize,
      fontWeight: FontWeight.bold,
    );
    final painter = TextPainter(
      text: TextSpan(text: letter.toUpperCase(), style: style),
      textDirection: TextDirection.ltr,
    )..layout();

    final origin = Offset(
      (canvasSize.width - painter.width) / 2,
      (canvasSize.height - painter.height) / 2,
    );

    final points = <Offset>[];
    const step = 18.0;
    for (var x = 0.0; x < painter.width; x += step) {
      for (var y = 0.0; y < painter.height; y += step) {
        points.add(origin + Offset(x, y));
      }
    }
    return points;
  }

  static Rect _letterBounds(String letter, Size canvasSize) {
    const fontSize = 300.0;
    final painter = TextPainter(
      text: TextSpan(
        text: letter.toUpperCase(),
        style: const TextStyle(fontSize: fontSize, fontWeight: FontWeight.bold),
      ),
      textDirection: TextDirection.ltr,
    )..layout();

    final origin = Offset(
      (canvasSize.width - painter.width) / 2,
      (canvasSize.height - painter.height) / 2,
    );
    return origin & Size(painter.width, painter.height);
  }

  static Rect _boundsOf(List<Offset> points) {
    var minX = points.first.dx;
    var maxX = points.first.dx;
    var minY = points.first.dy;
    var maxY = points.first.dy;
    for (final p in points) {
      minX = minX < p.dx ? minX : p.dx;
      maxX = maxX > p.dx ? maxX : p.dx;
      minY = minY < p.dy ? minY : p.dy;
      maxY = maxY > p.dy ? maxY : p.dy;
    }
    return Rect.fromLTRB(minX, minY, maxX, maxY);
  }

  static double _overlapRatio(Rect a, Rect b) {
    final intersection = a.intersect(b);
    if (intersection.isEmpty) return 0;
    final interArea = intersection.width * intersection.height;
    final unionArea = a.width * a.height + b.width * b.height - interArea;
    if (unionArea <= 0) return 0;
    return interArea / unionArea;
  }
}
