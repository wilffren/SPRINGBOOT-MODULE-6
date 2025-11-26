package main.java.com.example.HU4.application.usecases;

import com.example.HU4.domain.model.User;
import com.example.HU4.domain.ports.in.AuthUseCase;
import com.example.HU4.domain.ports.out.UserRepositoryPort;
import com.example.HU4.infrastructure.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUseCaseImpl implements AuthUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthUseCaseImpl(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String register(User user) {
        if (userRepositoryPort.existsByUsername(user.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        User saved = userRepositoryPort.save(user);
        return jwtService.generateToken(saved.getUsername(), saved.getRole());
    }

    @Override
    public String login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return jwtService.generateToken(user.getUsername(), user.getRole());
    }
}