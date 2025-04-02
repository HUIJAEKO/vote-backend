package project.votebackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.Vote;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    //투표한 글 + 내가 선택한 관심사 글
    @EntityGraph(attributePaths = {
            "reactions", "category", "user", "images", "options"
    })
    @Query("""
        SELECT v FROM Vote v
        WHERE v.user.userId = :userId OR v.category.categoryId IN :categoryIds
        ORDER BY v.createdAt DESC
    """)
    Page<Vote> findMainPageVotes(@Param("userId") Long userId, @Param("categoryIds") List<Long> categoryIds, Pageable pageable);


    //내가 투표한 글
    @EntityGraph(attributePaths = {
            "reactions", "category", "user", "images", "options"
    })
    @Query("""
        SELECT DISTINCT v FROM Vote v
        JOIN v.selections s
        WHERE s.user.userId = :userId
        ORDER BY v.createdAt DESC
    """)
    Page<Vote> findVotedByUserId(@Param("userId") Long userId, Pageable pageable);


    //내가 좋아요한 글
    @EntityGraph(attributePaths = {
            "reactions", "category", "user", "images", "options"
    })
    @Query("""
        SELECT DISTINCT v FROM Vote v
        JOIN v.reactions r
        WHERE r.user.userId = :userId AND r.reaction = 'LIKE'
        ORDER BY v.createdAt DESC
    """)
    Page<Vote> findLikedVotes(@Param("userId") Long userId, Pageable pageable);


    //내가 북마크한 글
    @EntityGraph(attributePaths = {
            "reactions", "category", "user", "images", "options"
    })
        @Query("""
        SELECT DISTINCT v FROM Vote v
        JOIN v.reactions r
        WHERE r.user.userId = :userId AND r.reaction = 'BOOKMARK'
        ORDER BY v.createdAt DESC
    """)
    Page<Vote> findBookmarkedVotes(@Param("userId") Long userId, Pageable pageable);
}
