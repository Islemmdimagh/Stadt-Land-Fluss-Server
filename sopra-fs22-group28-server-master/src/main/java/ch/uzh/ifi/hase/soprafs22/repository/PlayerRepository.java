package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("playerRepository")
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findById(long id);

    Player findByName(String name);

    Player findByPlayerToken(String token);
}
