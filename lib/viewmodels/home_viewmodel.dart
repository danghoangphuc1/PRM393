import 'package:flutter/material.dart';

class HomeViewModel extends ChangeNotifier {
  String _welcomeMessage = "Welcome to KIDIO!";
  String get welcomeMessage => _welcomeMessage;

  void updateMessage(String newMessage) {
    _welcomeMessage = newMessage;
    notifyListeners();
  }
}
