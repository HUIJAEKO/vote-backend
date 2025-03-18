package project.votebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.votebackend.domain.User;
import project.votebackend.dto.UserSignupDto;
import project.votebackend.repository.UserRepository;
import project.votebackend.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupDto userSignupDto) {
        User newUser = authService.registerUser(userSignupDto);
        return ResponseEntity.ok(newUser);
    }
}

