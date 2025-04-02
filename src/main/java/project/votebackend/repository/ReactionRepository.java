package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.Reaction;
import project.votebackend.domain.User;
import project.votebackend.domain.Vote;
import project.votebackend.type.ReactionType;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserAndVoteAndReaction(User user, Vote vote, ReactionType reaction);
}
