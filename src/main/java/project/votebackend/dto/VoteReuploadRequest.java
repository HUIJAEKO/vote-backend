package project.votebackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoteReuploadRequest {
    private LocalDateTime finishTime;
}
