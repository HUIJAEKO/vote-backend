package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.votebackend.domain.UserInterest;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
}
