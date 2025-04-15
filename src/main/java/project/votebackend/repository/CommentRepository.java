package project.votebackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //부모 댓글
    Page<Comment> findByVote_VoteIdAndParentIsNull(Long voteId, Pageable pageable);

    //자식 댓글
    List<Comment> findByParent_CommentIdOrderByCreatedAtAsc(Long parentId);

}
