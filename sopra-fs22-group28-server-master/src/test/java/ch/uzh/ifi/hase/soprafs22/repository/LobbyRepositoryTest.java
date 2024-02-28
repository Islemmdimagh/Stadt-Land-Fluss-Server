package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class LobbyRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private LobbyRepository lobbyRepository;

  @Test
  void findByID_success() {
    // given
    Lobby lobby = new Lobby();
    lobby.setHost("Bambo");
    lobby.setId(1L);
    lobby.setHostToken("12345678");

    entityManager.flush();

    // when
    Optional<Lobby> found = lobbyRepository.findById(lobby.getId());
    if (found.isPresent()){
    // then
        Lobby lubby = found.get();
    assertEquals(lubby.getHost(), lobby.getHost());
    assertEquals(lubby.getId(), lobby.getId());
    assertEquals(lubby.getHostToken(), lobby.getHostToken());
  }}
}
