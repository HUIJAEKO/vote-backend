package project.votebackend.dto;

import lombok.*;
import project.votebackend.type.ErrorCode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
