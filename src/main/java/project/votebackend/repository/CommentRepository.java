package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
