package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.votebackend.domain.Category;
import project.votebackend.domain.User;
import project.votebackend.domain.UserInterest;
import project.votebackend.dto.LoginRequest;
import project.votebackend.dto.LoginResponse;
import project.votebackend.dto.UserSignupDto;
import project.votebackend.exception.AuthException;
import project.votebackend.exception.CategoryException;
import project.votebackend.repository.CategoryRepository;
import project.votebackend.repository.UserInterestRepository;
import project.votebackend.repository.UserRepository;
import project.votebackend.type.ErrorCode;
import project.votebackend.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final UserInterestRepository userInterestRepository;
    private final JwtUtil jwtUtil;

    // 회원가입 (아이디 및 전화번호는 중복 존재 불가)
    @Transactional
    public User registerUser(UserSignupDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new AuthException(ErrorCode.ALREADY_EXIST_NAME);
        }

        if (userRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new AuthException(ErrorCode.ALREADY_EXIST_PHONE);
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword())) // 비밀번호 암호화
                .name(dto.getName())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .birthdate(dto.getBirthdate())
                .address(dto.getAddress())
                .introduction(dto.getIntroduction())
                .profileImage(dto.getProfileImage())
                .point(0L)
                .voteScore(0L)
                .build();

        User savedUser = userRepository.save(user);

        // 관심 카테고리 저장
        if (dto.getInterestCategory() != null) {
            for (Long categoryId : dto.getInterestCategory()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));
                UserInterest interest = UserInterest.builder()
                        .user(savedUser)
                        .category(category)
                        .build();
                userInterestRepository.save(interest);
            }
        }

        return savedUser;
    }

    //로그인
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getUserId());
        return new LoginResponse("success", token);
    }
}
