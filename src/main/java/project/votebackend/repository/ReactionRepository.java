package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.Reaction;
import project.votebackend.domain.User;
import project.votebackend.domain.Vote;
import project.votebackend.type.ReactionType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserAndVoteAndReaction(User user, Vote vote, ReactionType reaction);

    //리액션 수 카운트
    @Query("""
        SELECT r.vote.voteId, r.reaction, r.user.userId
        FROM Reaction r
        WHERE r.vote.voteId IN :voteIds
    """)
    List<Object[]> findReactionsByVoteIds(@Param("voteIds") List<Long> voteIds);
}
