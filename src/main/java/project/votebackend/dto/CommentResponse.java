package project.votebackend.dto;

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

    public CommentResponse(Comment comment) {
        this.id = comment.getCommentId();
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.likeCount = comment.getLikeCount();
        this.profileImage = comment.getUser().getProfileImage();
        this.parentId = comment.getParent() != null ? comment.getParent().getCommentId() : null;
    }
}
