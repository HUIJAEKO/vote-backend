package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.UserInterest;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
}
