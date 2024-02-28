package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Answer;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("answerRepository")
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Answer findById(long id);

    Answer findByLobby(Lobby lobby);

    Answer findByPlayerToken(String token);

    Answer findByPlayerID(Long playerId);
}
