package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.Follow;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Long countByFollowingId(Long followingId); //나를 팔로우하는 사람 수
    Long countByFollowerId(Long followerId); //내가 팔로잉하는 사람 수
}
