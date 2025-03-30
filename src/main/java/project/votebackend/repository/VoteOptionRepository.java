package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.VoteOption;

@Repository
public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
}
