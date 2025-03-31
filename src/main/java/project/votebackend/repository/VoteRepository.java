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
    @Query("SELECT v FROM Vote v WHERE v.user.userId = :userId OR v.category.categoryId IN :categoryIds")
    Page<Vote> findMainPageVotes(@Param("userId") Long userId, @Param("categoryIds") List<Long> categoryIds, Pageable pageable);
}
