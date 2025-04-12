package project.votebackend.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class UserPageDto {

    private String username;
    private String profileImage;
    private String introduction;
    private Long point;
    private Page<LoadVoteDto> posts;


}
