package project.votebackend.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //Auth
    ALREADY_EXIST_NAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    ALREADY_EXIST_PHONE(HttpStatus.CONFLICT, "이미 존재하는 전화번호입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"내부 서버 오류 발생"),
    USERNAME_NOT_FOUND(HttpStatus.UNAUTHORIZED, "존재하지 않는 아이디입니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    //Category
    CATEGORY_NOT_FOUND(HttpStatus.CONFLICT, "존재하지 않는 카테고리입니다.");


    private final HttpStatus httpStatus;
    private final String description;
}
