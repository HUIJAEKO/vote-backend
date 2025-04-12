package project.votebackend.exception;

import lombok.*;
import project.votebackend.type.ErrorCode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowException extends Exception{
    private ErrorCode errorCode;
    private String errorMessage;

    public FollowException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    public int getHttpStatus() {
        return errorCode.getHttpStatus().value();
    }
}
