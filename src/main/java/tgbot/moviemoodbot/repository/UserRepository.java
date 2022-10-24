package tgbot.moviemoodbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgbot.moviemoodbot.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
