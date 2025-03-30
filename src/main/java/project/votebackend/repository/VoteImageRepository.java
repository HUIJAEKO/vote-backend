package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.VoteImage;

@Repository
public interface VoteImageRepository extends JpaRepository<VoteImage, Long> {
}
