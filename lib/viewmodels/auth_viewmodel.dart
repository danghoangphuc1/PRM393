import 'package:flutter/material.dart';
import 'package:firebase_auth/firebase_auth.dart';
import '../services/auth_service.dart';

class AuthViewModel extends ChangeNotifier {
  final AuthService _authService = AuthService();

  User? _user;
  User? get user => _user;

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  AuthViewModel() {
    try {
      _authService.user.listen((User? user) {
        _user = user;
        notifyListeners();
      });
    } catch (e) {
      debugPrint("Firebase not initialized in this environment: $e");
    }
  }

  Future<bool> login(String email, String password) async {
    _isLoading = true;
    notifyListeners();

    final result = await _authService.loginWithEmail(email, password);

    _isLoading = false;
    notifyListeners();
    return result != null;
  }

  Future<bool> register(String email, String password) async {
    _isLoading = true;
    notifyListeners();

    final result = await _authService.registerWithEmail(email, password);

    _isLoading = false;
    notifyListeners();
    return result != null;
  }

  Future<void> logout() async {
    await _authService.signOut();
  }
}
