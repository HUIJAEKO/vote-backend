package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.votebackend.domain.User;
import project.votebackend.dto.UserSignupDto;
import project.votebackend.exception.AuthException;
import project.votebackend.repository.UserRepository;
import project.votebackend.type.ErrorCode;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public User registerUser(UserSignupDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new AuthException(ErrorCode.ALREADY_EXIST_NAME);
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword())) // 비밀번호 암호화
                .name(dto.getName())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .birthdate(dto.getBirthDate())
                .address(dto.getAddress())
                .introduction(dto.getIntroduction())
                .profileImage(dto.getProfileImage())
                .point(0L)
                .voteScore(0L)
                .build();

        return userRepository.save(user);
    }
}
