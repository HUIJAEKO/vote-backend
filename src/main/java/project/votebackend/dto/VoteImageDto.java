package project.votebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.votebackend.domain.VoteImage;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteImageDto {
    private Long id;
    private String imageUrl;

    public static VoteImageDto fromEntity(VoteImage image) {
        return VoteImageDto.builder()
                .id(image.getVoteImageId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
