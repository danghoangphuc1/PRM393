import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import '../core/theme/kidio_theme.dart';
import '../core/widgets/kidio_widgets.dart';
import '../viewmodels/fill_blank_viewmodel.dart';

class FillBlankGameView extends StatelessWidget {
  const FillBlankGameView({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => FillBlankViewModel(),
      child: const _FillBlankBody(),
    );
  }
}

class _FillBlankBody extends StatelessWidget {
  const _FillBlankBody();

  @override
  Widget build(BuildContext context) {
    final vm = context.watch<FillBlankViewModel>();
    final q = vm.current;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Fill the Blank'),
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
            StarRewardRow(stars: vm.stars),
            const SizedBox(height: 8),
            LinearProgressIndicator(
              value: (vm.questionIndex + 1) / vm.totalQuestions,
              minHeight: 10,
              borderRadius: BorderRadius.circular(8),
              backgroundColor: Colors.grey.shade200,
              color: KidioTheme.skyBlue,
            ),
            const SizedBox(height: 24),
            Text(q.emoji, style: const TextStyle(fontSize: 72)),
            const SizedBox(height: 16),
            AnimatedSwitcher(
              duration: const Duration(milliseconds: 300),
              child: RichText(
                key: ValueKey(vm.questionIndex),
                textAlign: TextAlign.center,
                text: TextSpan(
                  style: const TextStyle(
                    fontSize: 28,
                    fontWeight: FontWeight.w700,
                    color: Color(0xFF4E342E),
                  ),
                  children: [
                    TextSpan(text: '${q.sentenceBefore} '),
                    TextSpan(
                      text: vm.selected ?? '______',
                      style: TextStyle(
                        color: vm.lastCorrect == true
                            ? KidioTheme.grassGreen
                            : vm.lastCorrect == false
                                ? Colors.red
                                : KidioTheme.grapePurple,
                        decoration: TextDecoration.underline,
                      ),
                    ),
                    TextSpan(text: ' ${q.sentenceAfter}'),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 32),
            ...q.choices.map((choice) {
              final selected = vm.selected == choice;
              Color? bg;
              if (vm.answered) {
                if (choice == q.correctWord) {
                  bg = KidioTheme.grassGreen.withValues(alpha: 0.3);
                } else if (selected) {
                  bg = Colors.red.withValues(alpha: 0.2);
                }
              } else if (selected) {
                bg = KidioTheme.skyBlue.withValues(alpha: 0.2);
              }

              return Padding(
                padding: const EdgeInsets.only(bottom: 12),
                child: SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: vm.answered
                        ? null
                        : () {
                            vm.selectAnswer(choice);
                            SystemSound.play(
                              choice == q.correctWord
                                  ? SystemSoundType.alert
                                  : SystemSoundType.click,
                            );
                          },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: bg ?? Colors.white,
                      foregroundColor: const Color(0xFF4E342E),
                      minimumSize: const Size(double.infinity, 60),
                    ),
                    child: Text(
                      choice,
                      style: const TextStyle(fontSize: 22),
                    ),
                  ),
                ),
              );
            }),
            const Spacer(),
            if (vm.answered) ...[
              Text(
                vm.lastCorrect == true ? '🌟 Correct!' : '💪 Try again!',
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
                      onPressed: vm.retry,
                      child: const Text('Retry'),
                    ),
                  const SizedBox(width: 12),
                  ElevatedButton(
                    onPressed: vm.nextQuestion,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: KidioTheme.skyBlue,
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
