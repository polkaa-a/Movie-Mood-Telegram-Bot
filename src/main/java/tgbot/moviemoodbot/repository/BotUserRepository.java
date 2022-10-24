package tgbot.moviemoodbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgbot.moviemoodbot.model.BotUser;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, Long> {
}
