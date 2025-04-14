package project.votebackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.Vote;
import project.votebackend.type.ReactionType;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    //내가 작성한 글
    Page<Vote> findByUser_UserId(Long userId, Pageable pageable);

    //투표한 글 + 내가 선택한 관심사 글
    @EntityGraph(attributePaths = {
            "reactions", "category", "user", "images", "options"
    })
    @Query("""
        SELECT v FROM Vote v
        WHERE v.user.userId = :userId
           OR v.category.categoryId IN :categoryIds
           OR v.user.userId IN (
                SELECT f.followingId FROM Follow f WHERE f.followerId = :userId
           )
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


    //단일 글
    @Query("SELECT v FROM Vote v " +
            "JOIN FETCH v.user " +
            "LEFT JOIN FETCH v.options o " +
            "LEFT JOIN FETCH v.reactions r " +
            "LEFT JOIN FETCH v.images i " +
            "WHERE v.voteId = :voteId")
    Optional<Vote> findByIdWithUserAndOptions(@Param("voteId") Long voteId);

    //좋아요 상위 10개의 글
    @Query("""
        SELECT v
        FROM Vote v
        JOIN v.reactions r
        WHERE r.reaction = :reactionType
        GROUP BY v
        ORDER BY COUNT(r) DESC
    """)
    List<Vote> findTop10ByReactionTypeOrderByLikeCountDesc(
            @Param("reactionType") ReactionType reactionType,
            Pageable pageable
    );

    //사용자의 게시글 개수
    Long countByUser_UserId(Long userId);

    //특정 카테고리의 글 조회
    @Query("""
        SELECT v
        FROM Vote v
        LEFT JOIN v.reactions r ON r.reaction = 'LIKE'
        WHERE v.category.categoryId = :categoryId
        GROUP BY v
        ORDER BY COUNT(r) DESC
    """)
    Page<Vote> findByCategoryOrderByLikeCount(@Param("categoryId") Long categoryId, Pageable pageable);
}
