package project.votebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import project.votebackend.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private Long userId;
    private String username;
    private String name;
    private String content;
    private LocalDateTime createdAt;
    private String profileImage;
    private int likeCount;
    private Long parentId;

    @JsonProperty("isLiked")
    private boolean isLiked;
    private List<CommentResponse> replies;

    public static CommentResponse fromEntity(Comment comment, Long currentUserId) {
        boolean isLiked = comment.getCommentLikes().stream()
                .anyMatch(like -> like.getUser().getUserId().equals(currentUserId));

        return CommentResponse.builder()
                .id(comment.getCommentId())
                .userId(comment.getUser().getUserId())
                .username(comment.getUser().getUsername())
                .name(comment.getUser().getName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .profileImage(comment.getUser().getProfileImage())
                .likeCount(comment.getLikeCount())
                .parentId(comment.getParent() != null ? comment.getParent().getCommentId() : null)
                .isLiked(isLiked)
                .replies(null)
                .build();
    }

    // 대댓글 포함 버전
    public static CommentResponse fromEntityWithReplies(Comment parent, List<Comment> replies, Long currentUserId) {
        boolean isLiked = parent.getCommentLikes().stream()
                .anyMatch(like -> like.getUser().getUserId().equals(currentUserId));

        return CommentResponse.builder()
                .id(parent.getCommentId())
                .userId(parent.getUser().getUserId())
                .username(parent.getUser().getUsername())
                .content(parent.getContent())
                .createdAt(parent.getCreatedAt())
                .profileImage(parent.getUser().getProfileImage())
                .likeCount(parent.getLikeCount())
                .parentId(null)
                .isLiked(isLiked)
                .replies(replies.stream()
                        .map(reply -> fromEntity(reply, currentUserId))
                        .collect(Collectors.toList()))
                .build();
    }
}
