package com.tradigo.tradigo_backend.service;

import com.tradigo.tradigo_backend.config.JwtUtil;
import com.tradigo.tradigo_backend.dto.LoginRequest;
import com.tradigo.tradigo_backend.model.User;
import com.tradigo.tradigo_backend.repository.UserRepository;
import com.tradigo.tradigo_backend.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    public String register(RegisterRequest request) {

        if (userRepository.findByHospitalid(request.getHospitalid()).isPresent()) {
            throw new RuntimeException("Hospital ID already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .hospitalid(request.getHospitalid())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role("RURAL_WORKER")
                .build();

        userRepository.save(user);

        return "User registered successfully";
    }
    public String registerAdmin(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .hospitalid(request.getHospitalid())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role("HOSPITAL_ADMIN")
                .build();

        userRepository.save(user);
        return "Admin created";
    }

    public Object login(LoginRequest request) {

        System.out.println("Hospital ID received: " + request.getHospitalid());

        User user = userRepository.findByHospitalid(request.getHospitalid())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getHospitalid());

        return new java.util.HashMap<>() {{
            put("token", token);
            put("hospitalid", user.getHospitalid());
            put("name", user.getName());
            put("role", user.getRole());
        }};
    }
    public Object getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String hospitalId = authentication.getName();

        User user = userRepository.findByHospitalid(hospitalId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new java.util.HashMap<>() {{
            put("hospitalid", user.getHospitalid());
            put("name", user.getName());
            put("email", user.getEmail());
            put("role", user.getRole());
        }};
    }
}
