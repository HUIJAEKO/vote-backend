package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.User;
import project.votebackend.domain.Vote;
import project.votebackend.domain.VoteSelection;

import java.util.Optional;

@Repository
public interface VoteSelectRepository extends JpaRepository<VoteSelection, Long> {
    Optional<VoteSelection> findByUserAndVote(User user, Vote vote);
}
