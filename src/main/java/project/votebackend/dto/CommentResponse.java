package project.votebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import project.votebackend.domain.Comment;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private String profileImage;
    private int likeCount;
    private Long parentId;

    @JsonProperty("isLiked")
    private boolean isLiked;

    public static CommentResponse fromEntity(Comment comment, Long currentUserId) {
        boolean isLiked = comment.getCommentLikes().stream()
                .anyMatch(like -> like.getUser().getUserId().equals(currentUserId));

        CommentResponse dto = new CommentResponse();
        dto.id = comment.getCommentId();
        dto.username = comment.getUser().getUsername();
        dto.content = comment.getContent();
        dto.createdAt = comment.getCreatedAt();
        dto.profileImage = comment.getUser().getProfileImage();
        dto.likeCount = comment.getLikeCount();
        dto.parentId = comment.getParent() != null ? comment.getParent().getCommentId() : null;
        dto.isLiked = isLiked;
        return dto;
    }
}
