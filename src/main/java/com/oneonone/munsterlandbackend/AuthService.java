package com.oneonone.munsterlandbackend;
import com.oneonone.munsterlandbackend.models.SignupRequestDto;
import com.oneonone.munsterlandbackend.models.SignupResponse;
import com.oneonone.munsterlandbackend.models.UserEntity;
import com.oneonone.munsterlandbackend.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oneonone.munsterlandbackend.models.LoginResponse;
import com.oneonone.munsterlandbackend.models.LoginRequest;
import io.jsonwebtoken.*;
import java.util.*;
import com.oneonone.munsterlandbackend.services.JwtService;
import com.oneonone.munsterlandbackend.models.UserKind;
import com.oneonone.munsterlandbackend.models.UserRole;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository users;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwt;

  @Transactional
  public SignupResponse signup(SignupRequestDto req) {
    // prevent ADMIN via API
    if (req.kind().name().equals("ADMIN")) {
      throw new IllegalArgumentException("ADMIN registration is not allowed");
    }

    if (users.existsByEmail(req.email())) {
      throw new IllegalArgumentException("Email already registered");
    }

    // 🔒 validate kind-role combination
    if (req.kind() == UserKind.NEWCOMER) {
      if (!(req.role() == UserRole.STUDENT || req.role() == UserRole.VISITOR || req.role() == UserRole.PROFESSIONAL)) {
          throw new IllegalArgumentException("Newcomer must have role STUDENT, VISITOR, or PROFESSIONAL");
      }
    }

    if (req.kind() == UserKind.VOLUNTEER) {
        if (!(req.role() == UserRole.CITIZEN || req.role() == UserRole.IMMIGRANT)) {
            throw new IllegalArgumentException("Volunteer must have role CITIZEN or IMMIGRANT");
        }
    }

    var entity = UserEntity.builder()
        .email(req.email().toLowerCase())
        .passwordHash(passwordEncoder.encode(req.password()))
        .fullName(req.fullName())
        .kind(req.kind())
        .role(req.role())
        .city(req.city() == null ? "Münster" : req.city())
        .about(req.about())
        .points(0)
        .build();

    var saved = users.save(entity);

    return new SignupResponse(
        saved.getId().toString(),
        saved.getEmail(),
        saved.getFullName(),
        saved.getKind().name(),
        saved.getRole().name()
    );
  }

  // NEW: login
  @Transactional(readOnly = true)
  public LoginResponse login(LoginRequest req) {
    var user = users.findByEmail(req.email().toLowerCase())
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

    if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    var token = jwt.generate(
        user.getEmail(),
        Map.of(
            "uid", user.getId().toString(),
            "kind", user.getKind().name(),
            "role", user.getRole().name()
        )
    );

    return new LoginResponse(
        token,
        user.getId().toString(),
        user.getEmail(),
        user.getFullName(),
        user.getKind().name(),
        user.getRole().name()
    );
  }
}
