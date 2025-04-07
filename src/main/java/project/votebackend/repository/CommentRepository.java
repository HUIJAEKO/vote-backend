    package project.votebackend.repository;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import project.votebackend.domain.Comment;

    import java.util.List;

    @Repository
    public interface CommentRepository extends JpaRepository<Comment, Long> {
        List<Comment> findByVote_VoteIdAndParentIsNullOrderByCreatedAtDesc(Long voteId); // 최신순 정렬
    }
