import 'package:flutter/material.dart';
import '../theme/kidio_theme.dart';

class KidioMenuButton extends StatelessWidget {
  final String title;
  final IconData icon;
  final Color color;
  final VoidCallback onTap;

  const KidioMenuButton({
    super.key,
    required this.title,
    required this.icon,
    required this.color,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6),
      child: Material(
        elevation: 8,
        shadowColor: color.withValues(alpha: 0.45),
        borderRadius: BorderRadius.circular(28),
        child: InkWell(
          onTap: onTap,
          borderRadius: BorderRadius.circular(28),
          child: Container(
            width: double.infinity,
            constraints: const BoxConstraints(maxWidth: 300, minHeight: 68),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(28),
              gradient: LinearGradient(
                colors: [
                  Colors.white,
                  color.withValues(alpha: 0.12),
                ],
              ),
              border: Border.all(color: color.withValues(alpha: 0.55), width: 3),
            ),
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 10),
              child: Row(
                children: [
                  Container(
                    width: 52,
                    height: 52,
                    decoration: BoxDecoration(
                      color: color.withValues(alpha: 0.22),
                      shape: BoxShape.circle,
                    ),
                    child: Icon(icon, color: color, size: 30),
                  ),
                  const SizedBox(width: 14),
                  Expanded(
                    child: Text(
                      title,
                      style: TextStyle(
                        fontSize: 24,
                        fontWeight: FontWeight.w800,
                        color: color.darken(),
                      ),
                    ),
                  ),
                  Icon(Icons.arrow_forward_ios_rounded, color: color, size: 18),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class StarRewardRow extends StatelessWidget {
  final int stars;
  final int maxStars;

  const StarRewardRow({super.key, required this.stars, this.maxStars = 3});

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: List.generate(maxStars, (i) {
        return Icon(
          i < stars ? Icons.star_rounded : Icons.star_outline_rounded,
          color: i < stars ? KidioTheme.sunnyYellow : Colors.grey.shade300,
          size: 40,
        );
      }),
    );
  }
}

class ScoreBadge extends StatelessWidget {
  final int score;
  final String label;

  const ScoreBadge({super.key, required this.score, this.label = 'Score'});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
      decoration: BoxDecoration(
        color: KidioTheme.grassGreen,
        borderRadius: BorderRadius.circular(18),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          const Icon(Icons.emoji_events, color: Colors.white, size: 22),
          const SizedBox(width: 6),
          Text(
            '$label: $score',
            style: const TextStyle(
              color: Colors.white,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ],
      ),
    );
  }
}

extension _ColorExt on Color {
  Color darken() {
    final hsl = HSLColor.fromColor(this);
    return hsl.withLightness((hsl.lightness * 0.65).clamp(0.0, 1.0)).toColor();
  }
}
