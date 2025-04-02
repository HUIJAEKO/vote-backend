package project.votebackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.Vote;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query(
            value = "SELECT * FROM vote v WHERE v.user_id = :userId OR v.category_id IN (:categoryIds)",
            countQuery = "SELECT count(*) FROM vote v WHERE v.user_id = :userId OR v.category_id IN (:categoryIds)",
            nativeQuery = true
    )
    Page<Vote> findMainPageVotes(@Param("userId") Long userId, @Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    @Query(
        value = """
            SELECT DISTINCT v.* FROM vote v
            JOIN vote_selections s ON v.vote_id = s.vote_id
            WHERE s.user_id = :userId
            ORDER BY v.created_at DESC
            """,
        countQuery = """
            SELECT COUNT(DISTINCT v.vote_id) FROM vote v
            JOIN vote_selections s ON v.vote_id = s.vote_id
            WHERE s.user_id = :userId
            """,
        nativeQuery = true
    )
    Page<Vote> findVotedByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(
        value = """
            SELECT DISTINCT v.* FROM vote v
            JOIN reaction r ON v.vote_id = r.vote_id
            WHERE r.user_id = :userId AND r.reaction = 'LIKE'
            ORDER BY v.created_at DESC
            """,
        countQuery = """
            SELECT COUNT(DISTINCT v.vote_id) FROM vote v
            JOIN reaction r ON v.vote_id = r.vote_id
            WHERE r.user_id = :userId AND r.reaction = 'LIKE'
            """,
        nativeQuery = true
    )
    Page<Vote> findLikedVotes(@Param("userId") Long userId, Pageable pageable);

    @Query(
        value = """
            SELECT DISTINCT v.* FROM vote v
            JOIN reaction r ON v.vote_id = r.vote_id
            WHERE r.user_id = :userId AND r.reaction = 'BOOKMARK'
            ORDER BY v.created_at DESC
            """,
        countQuery = """
            SELECT COUNT(DISTINCT v.vote_id) FROM vote v
            JOIN reaction r ON v.vote_id = r.vote_id
            WHERE r.user_id = :userId AND r.reaction = 'BOOKMARK'
            """,
        nativeQuery = true
    )
    Page<Vote> findBookmarkedVotes(@Param("userId") Long userId, Pageable pageable);
}
