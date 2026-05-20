import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../viewmodels/pronunciation_viewmodel.dart';

class PronunciationCoachView extends StatelessWidget {
  final String targetWord;

  const PronunciationCoachView({super.key, required this.targetWord});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Pronunciation Coach'),
        backgroundColor: Colors.redAccent,
        foregroundColor: Colors.white,
      ),
      body: Consumer<PronunciationViewModel>(
        builder: (context, viewModel, child) {
          return Center(
            child: Padding(
              padding: const EdgeInsets.all(32.0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text(
                    'Can you say:',
                    style: TextStyle(fontSize: 24, color: Colors.grey),
                  ),
                  Text(
                    targetWord,
                    style: const TextStyle(
                      fontSize: 60,
                      fontWeight: FontWeight.bold,
                      color: Colors.redAccent,
                    ),
                  ),
                  const SizedBox(height: 50),
                  Container(
                    padding: const EdgeInsets.all(20),
                    decoration: BoxDecoration(
                      color: Colors.grey.shade100,
                      borderRadius: BorderRadius.circular(20),
                      border: Border.all(color: Colors.redAccent.withOpacity(0.3)),
                    ),
                    child: Text(
                      viewModel.words.isEmpty ? '...' : viewModel.words,
                      style: const TextStyle(fontSize: 30, fontStyle: FontStyle.italic),
                      textAlign: TextAlign.center,
                    ),
                  ),
                  const SizedBox(height: 30),
                  Text(
                    viewModel.feedback,
                    style: TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      color: viewModel.feedback.contains('Excellent') ? Colors.green : Colors.orange,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 60),
                  GestureDetector(
                    onTapDown: (_) => viewModel.startCoach(targetWord),
                    onTapUp: (_) => viewModel.stopCoach(),
                    child: CircleAvatar(
                      radius: 50,
                      backgroundColor: viewModel.isListening ? Colors.green : Colors.redAccent,
                      child: Icon(
                        viewModel.isListening ? Icons.mic : Icons.mic_none,
                        size: 50,
                        color: Colors.white,
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  Text(
                    viewModel.isListening ? 'RELEASE TO STOP' : 'HOLD TO SPEAK',
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
