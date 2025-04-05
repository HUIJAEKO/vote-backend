package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.votebackend.domain.Comment;
import project.votebackend.domain.User;
import project.votebackend.domain.Vote;
import project.votebackend.dto.CommentResponse;
import project.votebackend.exception.AuthException;
import project.votebackend.exception.CommentException;
import project.votebackend.exception.VoteException;
import project.votebackend.repository.CommentRepository;
import project.votebackend.repository.UserRepository;
import project.votebackend.repository.VoteRepository;
import project.votebackend.type.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    //댓글 작성
    @Transactional
    public Comment createComment(Long voteId, String content, String username, Long parentId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteException(ErrorCode.VOTE_NOT_FOUND));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setVote(vote);
        comment.setContent(content);

        if (parentId != null) {
            Comment parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new CommentException(ErrorCode.PARENT_COMMENT_NOT_FOUND));
            comment.setParent(parent);
        }

        return commentRepository.save(comment);
    }

    //댓글 조회
    public List<CommentResponse> getComments(Long voteId) {
        List<Comment> comment = commentRepository
                .findByVote_VoteIdAndParentIsNullOrderByCreatedAtDesc(voteId);

        return comment.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }
}
