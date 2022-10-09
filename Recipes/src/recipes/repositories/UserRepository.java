package recipes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import recipes.models.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);
}