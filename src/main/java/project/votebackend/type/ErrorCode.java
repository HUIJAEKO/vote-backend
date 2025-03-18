package project.votebackend.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ALREADY_EXIST_NAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"내부 서버 오류 발생");

    private final HttpStatus httpStatus;
    private final String description;
}
