import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import '../core/theme/kidio_theme.dart';
import '../core/widgets/kidio_widgets.dart';
import '../models/learning_item.dart';
import '../viewmodels/listen_choose_viewmodel.dart';

class ListenChooseGameView extends StatelessWidget {
  const ListenChooseGameView({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => ListenChooseViewModel(),
      child: const _ListenChooseBody(),
    );
  }
}

class _ListenChooseBody extends StatelessWidget {
  const _ListenChooseBody();

  @override
  Widget build(BuildContext context) {
    final vm = context.watch<ListenChooseViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Listen & Choose'),
        actions: [
          Padding(
            padding: const EdgeInsets.only(right: 8),
            child: Center(child: ScoreBadge(score: vm.score)),
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            LinearProgressIndicator(
              value: vm.progress,
              minHeight: 10,
              borderRadius: BorderRadius.circular(8),
              color: KidioTheme.candyPink,
            ),
            const SizedBox(height: 8),
            Text(
              'Round ${vm.roundIndex + 1} of ${vm.totalRounds}',
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 24),
            ElevatedButton.icon(
              onPressed: vm.playWord,
              icon: const Icon(Icons.volume_up_rounded, size: 32),
              label: Text(
                vm.played ? 'Play Again' : 'Play Word',
                style: const TextStyle(fontSize: 22),
              ),
              style: ElevatedButton.styleFrom(
                backgroundColor: KidioTheme.grapePurple,
                foregroundColor: Colors.white,
                minimumSize: const Size(260, 72),
              ),
            ),
            const SizedBox(height: 8),
            const Text(
              'Tap the picture you hear!',
              style: TextStyle(fontSize: 20),
            ),
            const SizedBox(height: 20),
            Expanded(
              child: GridView.count(
                crossAxisCount: 2,
                mainAxisSpacing: 14,
                crossAxisSpacing: 14,
                children: vm.current.options.map((item) {
                  return _OptionCard(
                    item: item,
                    selected: vm.selectedId == item.id,
                    showResult: vm.answered,
                    isCorrect: item.id == vm.current.correct.id,
                    onTap: vm.answered ? null : () => vm.selectOption(item),
                  );
                }).toList(),
              ),
            ),
            if (vm.answered) ...[
              Text(
                vm.lastCorrect == true ? '🎉 You got it!' : 'Not quite!',
                style: TextStyle(
                  fontSize: 26,
                  fontWeight: FontWeight.bold,
                  color: vm.lastCorrect == true
                      ? KidioTheme.grassGreen
                      : Colors.orange,
                ),
              ),
              const SizedBox(height: 12),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  if (vm.lastCorrect == false)
                    OutlinedButton(
                      onPressed: () {
                        vm.retryRound();
                        vm.playWord();
                      },
                      child: const Text('Retry'),
                    ),
                  const SizedBox(width: 12),
                  ElevatedButton(
                    onPressed: () {
                      vm.nextRound();
                      if (vm.roundIndex == 0 && vm.answered == false) {
                        // fresh round — auto play optional
                      }
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: KidioTheme.candyPink,
                      foregroundColor: Colors.white,
                    ),
                    child: const Text('Next'),
                  ),
                ],
              ),
            ],
          ],
        ),
      ),
    );
  }
}

class _OptionCard extends StatelessWidget {
  final LearningItem item;
  final bool selected;
  final bool showResult;
  final bool isCorrect;
  final VoidCallback? onTap;

  const _OptionCard({
    required this.item,
    required this.selected,
    required this.showResult,
    required this.isCorrect,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    Color border = Colors.grey.shade300;
    if (showResult) {
      if (isCorrect) {
        border = KidioTheme.grassGreen;
      } else if (selected) {
        border = Colors.red;
      }
    } else if (selected) {
      border = KidioTheme.skyBlue;
    }

    return Material(
      color: Colors.white,
      elevation: 6,
      borderRadius: BorderRadius.circular(20),
      child: InkWell(
        onTap: onTap == null
            ? null
            : () {
                onTap!();
                SystemSound.play(SystemSoundType.click);
              },
        borderRadius: BorderRadius.circular(20),
        child: Container(
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(20),
            border: Border.all(color: border, width: 4),
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(item.emoji, style: const TextStyle(fontSize: 48)),
              const SizedBox(height: 8),
              Text(
                item.word,
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
