package com.example.HU4.domain.ports.in;

import com.example.HU4.domain.model.User;

public interface AuthUseCase {
    String register(User user);
    String login(String username, String password);
}