package project.votebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.votebackend.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPhone(String phone);
}
