package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
