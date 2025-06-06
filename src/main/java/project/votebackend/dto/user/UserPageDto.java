package project.votebackend.dto.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import project.votebackend.dto.vote.LoadVoteDto;

import java.time.LocalDateTime;

@Data
@Builder
public class UserPageDto {

    private String username;
    private String name;
    private String profileImage;
    private String introduction;
    private Long point;
    private Long postCount;
    private Long participatedCount;
    private LocalDateTime createdAt;
}
