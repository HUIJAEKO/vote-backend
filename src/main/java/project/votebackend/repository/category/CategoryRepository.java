package project.votebackend.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.domain.category.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
