package project.votebackend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VoteUpdateRequest {
    private String title;
    private String content;
    private Long categoryId;
    private List<String> options;
    private LocalDateTime finishTime;
}
