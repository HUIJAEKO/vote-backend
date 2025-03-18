package project.votebackend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import project.votebackend.dto.UserSignupDto;
import project.votebackend.type.Gender;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void 회원가입_성공() {
        UserSignupDto requestDto = UserSignupDto.builder()
                .username("test12")
                .password("password123")
                .name("테스트 사용자")
                .gender(Gender.MALE)
                .phone("010123458")
                .birthdate(LocalDate.of(1990,1,2))
                .address("서울특별시 강남구")
                .introduction("안녕하세요. 테스트 사용자입니다.")
                .profileImage("profile.jpg")
                .build();

        String url = "/auth/signup";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserSignupDto> requestEntity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
