package project.votebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.votebackend.domain.Vote;
import project.votebackend.type.ReactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoadMainPageVoteDto {
    private Long voteId;
    private String title;
    private String content;
    private String categoryName;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private List<VoteImageDto> images;
    private List<VoteOptionDto> voteOptions;
    private LocalDateTime finishTime;
    private int commentCount;
    private int likeCount;
    private boolean isBookmarked;
    private boolean isLiked;

    //Entity -> Dto 변환 후 반환
    public static LoadMainPageVoteDto fromEntity(Vote vote, Long currentUserId) {
        int commentCount = vote.getComments() != null ? vote.getComments().size() : 0;
        int likeCount = (int) vote.getReactions().stream()
                .filter(r -> r.getReaction() == ReactionType.LIKE)
                .count();

        boolean isLiked = vote.getReactions().stream()
                .anyMatch(r -> r.getUser().getUserId().equals(currentUserId)
                        && r.getReaction() == ReactionType.LIKE);

        boolean isBookmarked = vote.getReactions().stream()
                .anyMatch(r -> r.getUser().getUserId().equals(currentUserId)
                        && r.getReaction() == ReactionType.BOOKMARK);

        List<VoteImageDto> images = vote.getImages().stream()
                .map(VoteImageDto::fromEntity)
                .collect(Collectors.toList());

        List<VoteOptionDto> voteOptions = vote.getOptions().stream()
                .map(VoteOptionDto::fromEntity)
                .collect(Collectors.toList());

        return LoadMainPageVoteDto.builder()
                .voteId(vote.getVoteId())
                .title(vote.getTitle())
                .content(vote.getContent())
                .categoryName(vote.getCategory().getName())
                .userId(vote.getUser().getUserId())
                .username(vote.getUser().getUsername())
                .createdAt(vote.getCreatedAt())
                .finishTime(vote.getFinishTime())
                .images(images)
                .voteOptions(voteOptions)
                .commentCount(commentCount)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .isBookmarked(isBookmarked)
                .build();
    }
}
