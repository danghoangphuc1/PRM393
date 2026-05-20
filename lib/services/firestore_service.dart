import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';

class FirestoreService {
  FirebaseFirestore get _db => FirebaseFirestore.instance;
  FirebaseAuth get _auth => FirebaseAuth.instance;

  // Lưu điểm số luyện viết
  Future<void> saveWritingScore(String letter, double score) async {
    User? user = _auth.currentUser;
    if (user == null) return;

    await _db.collection('users').doc(user.uid).collection('writing_scores').add({
      'letter': letter,
      'score': score,
      'timestamp': FieldValue.serverTimestamp(),
    });

    // Cập nhật điểm cao nhất và tiến trình tổng thể
    await _db.collection('users').doc(user.uid).set({
      'last_active': FieldValue.serverTimestamp(),
      'total_practice_sessions': FieldValue.increment(1),
    }, SetOptions(merge: true));
  }

  // Lấy lịch sử điểm số
  Stream<QuerySnapshot> getWritingHistory() {
    User? user = _auth.currentUser;
    if (user == null) return const Stream.empty();

    return _db
        .collection('users')
        .doc(user.uid)
        .collection('writing_scores')
        .orderBy('timestamp', descending: true)
        .snapshots();
  }
}
