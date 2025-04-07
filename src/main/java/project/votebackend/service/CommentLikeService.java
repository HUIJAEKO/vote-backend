package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.votebackend.domain.Comment;
import project.votebackend.domain.CommentLike;
import project.votebackend.domain.User;
import project.votebackend.exception.AuthException;
import project.votebackend.exception.CommentException;
import project.votebackend.repository.CommentLikeRepository;
import project.votebackend.repository.CommentRepository;
import project.votebackend.repository.UserRepository;
import project.votebackend.type.ErrorCode;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    //댓글 좋아요
    @Transactional
    public boolean like(Long commentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUNT));

        return commentLikeRepository.findByUserAndComment(user, comment)
                .map(existing -> {
                    commentLikeRepository.delete(existing);
                    comment.setLikeCount(comment.getLikeCount() - 1);
                    return false;
                })
                .orElseGet(() -> {
                    CommentLike like = new CommentLike();
                    like.setUser(user);
                    like.setComment(comment);
                    commentLikeRepository.save(like);
                    comment.setLikeCount(comment.getLikeCount() + 1);
                    return true;
                });
    }

    //좋아요 수 계산
    public long getLikeCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUNT));
        return comment.getLikeCount();
    }
}
