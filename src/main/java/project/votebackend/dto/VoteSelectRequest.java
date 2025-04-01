package project.votebackend.dto;

import lombok.Data;

@Data
public class VoteSelectRequest {
    private Long userId;
    private Long voteId;
    private Long optionId;
}
