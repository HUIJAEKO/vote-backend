package project.votebackend.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class MyPageDto {

    private String username;
    private String profileImage;
    private String introduction;
    private Long point;
    private Page<LoadVoteDto> posts;


}
