import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import '../core/theme/kidio_theme.dart';
import '../core/widgets/kidio_widgets.dart';
import '../viewmodels/word_build_viewmodel.dart';

class WordBuildGameView extends StatelessWidget {
  const WordBuildGameView({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => WordBuildViewModel(),
      child: const _WordBuildBody(),
    );
  }
}

class _WordBuildBody extends StatelessWidget {
  const _WordBuildBody();

  @override
  Widget build(BuildContext context) {
    final vm = context.watch<WordBuildViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Build the Word'),
        actions: [
          Padding(
            padding: const EdgeInsets.only(right: 12),
            child: Center(child: ScoreBadge(score: vm.score)),
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Text(
              'Level ${vm.currentPuzzle.level} • ${vm.puzzleIndex + 1}/${vm.totalPuzzles}',
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 12),
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: KidioTheme.sunnyYellow.withValues(alpha: 0.35),
                shape: BoxShape.circle,
              ),
              child: Text(
                vm.currentPuzzle.emoji,
                style: const TextStyle(fontSize: 72),
              ),
            ),
            const SizedBox(height: 8),
            Text(
              'What is this?',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.w600,
                color: Colors.grey.shade600,
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 12),
              child: Text(
                vm.currentPuzzle.hint,
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 15, color: Colors.grey.shade700),
              ),
            ),
            const SizedBox(height: 20),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: List.generate(vm.slots.length, (i) {
                return Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 4),
                  child: DragTarget<String>(
                    onWillAccept: (letter) =>
                        letter != null &&
                        vm.slots[i] == null &&
                        !vm.isCompleted,
                    onAccept: (letter) {
                      vm.placeLetter(letter, i);
                      if (vm.isCompleted) {
                        SystemSound.play(SystemSoundType.alert);
                      }
                    },
                    builder: (context, candidate, rejected) {
                      final filled = vm.slots[i];
                      final highlight = candidate.isNotEmpty;
                      return GestureDetector(
                        onTap: filled != null ? () => vm.removeFromSlot(i) : null,
                        child: AnimatedContainer(
                          duration: const Duration(milliseconds: 200),
                          width: 52,
                          height: 58,
                          alignment: Alignment.center,
                          decoration: BoxDecoration(
                            color: highlight
                                ? KidioTheme.sunnyYellow.withValues(alpha: 0.5)
                                : filled != null
                                    ? KidioTheme.grassGreen.withValues(alpha: 0.3)
                                    : Colors.white,
                            borderRadius: BorderRadius.circular(14),
                            border: Border.all(
                              color: KidioTheme.grassGreen,
                              width: 3,
                            ),
                          ),
                          child: Text(
                            filled ?? '',
                            style: const TextStyle(
                              fontSize: 28,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                      );
                    },
                  ),
                );
              }),
            ),
            const SizedBox(height: 28),
            Wrap(
              spacing: 10,
              runSpacing: 10,
              alignment: WrapAlignment.center,
              children: vm.letterPool.map((letter) {
                return Draggable<String>(
                  data: letter,
                  feedback: _LetterChip(letter: letter, dragging: true),
                  childWhenDragging: Opacity(
                    opacity: 0.3,
                    child: _LetterChip(letter: letter),
                  ),
                  onDragStarted: () => vm.setDragging(letter),
                  onDragEnd: (_) => vm.setDragging(null),
                  child: _LetterChip(letter: letter),
                );
              }).toList(),
            ),
            const Spacer(),
            if (vm.isCompleted)
              Column(
                children: [
                  const Text(
                    '🎉 Great job!',
                    style: TextStyle(
                      fontSize: 32,
                      fontWeight: FontWeight.w800,
                      color: KidioTheme.grassGreen,
                    ),
                  ),
                  const SizedBox(height: 12),
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton(
                      onPressed: vm.nextPuzzle,
                      style: ElevatedButton.styleFrom(
                        backgroundColor: KidioTheme.grassGreen,
                        foregroundColor: Colors.white,
                        minimumSize: const Size(double.infinity, 56),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(28),
                        ),
                      ),
                      child: const Text('Next Word'),
                    ),
                  ),
                ],
              )
            else
              TextButton(
                onPressed: vm.resetCurrent,
                child: const Text('Reset letters'),
              ),
          ],
        ),
      ),
    );
  }
}

class _LetterChip extends StatelessWidget {
  final String letter;
  final bool dragging;

  const _LetterChip({required this.letter, this.dragging = false});

  @override
  Widget build(BuildContext context) {
    return Material(
      elevation: dragging ? 12 : 6,
      borderRadius: BorderRadius.circular(16),
      color: KidioTheme.candyPink,
      child: Container(
        width: 56,
        height: 56,
        alignment: Alignment.center,
        child: Text(
          letter,
          style: const TextStyle(
            fontSize: 30,
            fontWeight: FontWeight.bold,
            color: Colors.white,
          ),
        ),
      ),
    );
  }
}
