package project.votebackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.votebackend.domain.User;
import project.votebackend.dto.UserSignupDto;
import project.votebackend.exception.AuthException;
import project.votebackend.repository.UserRepository;
import project.votebackend.type.ErrorCode;
import project.votebackend.type.Gender;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

//    @InjectMocks
//    private AuthService authService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @DisplayName("회원가입 성공")
//    @Test
//    void 회원가입_성공() {
//        // given
//        UserSignupDto dto = new UserSignupDto("testUser", "password", "홍길동", Gender.MALE,
//                "01012345678", LocalDate.of(2000,2,3), "Seoul", "안녕하세요!", "image_url");
//
//        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
//        when(userRepository.findByPhone(dto.getPhone())).thenReturn(Optional.empty());
//        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encryptedPassword");
//
//        User savedUser = User.builder()
//                .username(dto.getUsername())
//                .password("encryptedPassword")
//                .name(dto.getName())
//                .gender(dto.getGender())
//                .phone(dto.getPhone())
//                .birthdate(dto.getBirthdate())
//                .address(dto.getAddress())
//                .introduction(dto.getIntroduction())
//                .profileImage(dto.getProfileImage())
//                .point(0L)
//                .voteScore(0L)
//                .build();
//
//        when(userRepository.save(any(User.class))).thenReturn(savedUser);
//
//        // when
//        User result = authService.registerUser(dto);
//
//        // then
//        assertEquals(dto.getUsername(), result.getUsername());
//        assertEquals("encryptedPassword", result.getPassword());
//        assertEquals(dto.getPhone(), result.getPhone());
//
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @DisplayName("회원가입 아이디 중복")
//    @Test
//    void 회원가입_실패_아이디중복() {
//        // given
//        UserSignupDto dto = new UserSignupDto("testUser", "password", "홍길동", Gender.MALE,
//                "01012345678", LocalDate.of(2000,2,3), "Seoul", "안녕하세요!", "image_url");
//
//        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.of(new User()));
//
//        // when & then
//        AuthException exception = assertThrows(AuthException.class, () -> authService.registerUser(dto));
//        assertEquals(ErrorCode.ALREADY_EXIST_NAME, exception.getErrorCode());
//    }
//
//    @DisplayName("회원가입 전화번호 중복")
//    @Test
//    void 회원가입_실패_전화번호중복() {
//        // given
//        UserSignupDto dto = new UserSignupDto("testUser", "password", "홍길동", Gender.MALE,
//                "01012345678", LocalDate.of(2000,2,3), "Seoul", "안녕하세요!", "image_url");
//
//        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
//        when(userRepository.findByPhone(dto.getPhone())).thenReturn(Optional.of(new User()));
//
//        // when & then
//        AuthException exception = assertThrows(AuthException.class, () -> authService.registerUser(dto));
//        assertEquals(ErrorCode.ALREADY_EXIST_PHONE, exception.getErrorCode());
//    }
}
