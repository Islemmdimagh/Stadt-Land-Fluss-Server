package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see LobbyService
 */

@WebAppConfiguration
@SpringBootTest
public class LobbyServiceIntegrationTest {

  @Qualifier("lobbyRepository")
  @Autowired
  private LobbyRepository lobbyRepository;

  @Autowired
  private LobbyService lobbyService;

  @BeforeEach
  public void setup() {
    lobbyRepository.deleteAll();
  }

    @Test
    void createLobby_validInputs_success() {
        // given
        assertTrue(lobbyRepository.findAll().isEmpty());

        Lobby testLobby = new Lobby();
        testLobby.setHost("TestHost");
        testLobby.setRounds(1);
        testLobby.setHostToken("1234");
        testLobby.setCurrentLetter('a');
        testLobby.setRoundDuration(1);
        testLobby.setRand(new Random());
        testLobby.setRoundStarted(true);
        testLobby.setCategoryCitiesActive(true);
        testLobby.setCategoryCountriesActive(true);
        testLobby.setCategoryMunicipalityActive(false);
        testLobby.setCategoryRiversActive(false);
        List<Character> list = new ArrayList<>();
        list.add('a');
        list.add('b');
        testLobby.setUsedLetters(list);

        // when
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        // then
        assertEquals(testLobby.getId(), createdLobby.getId());
        assertEquals(testLobby.getHost(), createdLobby.getHost());
        assertEquals(testLobby.getRounds(), createdLobby.getRounds());
        assertEquals(testLobby.getRoundDuration(), createdLobby.getRoundDuration());
        assertEquals(testLobby.getCurrentLetter(), createdLobby.getCurrentLetter());
        assertEquals(testLobby.getCategoryCitiesActive(), createdLobby.getCategoryCitiesActive());
        assertEquals(testLobby.getCategoryRiversActive(), createdLobby.getCategoryRiversActive());
        assertEquals(testLobby.getCategoryCountriesActive(), createdLobby.getCategoryCountriesActive());
        assertEquals(testLobby.getCategoryMunicipalityActive(), createdLobby.getCategoryMunicipalityActive());
        assertEquals(testLobby.getRoundStarted(), createdLobby.getRoundStarted());
        assertNotNull(createdLobby.getHostToken());
    }
    @Test
    void getLobbyById_validInputs_success() {
        // given
        assertTrue(lobbyRepository.findAll().isEmpty());

        Lobby testLobby = new Lobby();
        testLobby.setHost("TestHost");
        testLobby.setRounds(1);
        testLobby.setHostToken("1234");
        testLobby.setCurrentLetter('a');
        testLobby.setRoundDuration(1);
        testLobby.setRand(new Random());
        testLobby.setRoundStarted(true);
        testLobby.setCategoryCitiesActive(true);
        testLobby.setCategoryCountriesActive(true);
        testLobby.setCategoryMunicipalityActive(false);
        testLobby.setCategoryRiversActive(false);
        List<Character> list = new ArrayList<>();
        list.add('a');
        list.add('b');
        testLobby.setUsedLetters(list);
        lobbyService.createLobby(testLobby);
        // when
        Lobby createdLobby = lobbyService.getLobbyById(testLobby.getId());
        // then
        assertEquals(testLobby.getId(), createdLobby.getId());
        assertEquals(testLobby.getHost(), createdLobby.getHost());
        assertEquals(testLobby.getRounds(), createdLobby.getRounds());
        assertEquals(testLobby.getRoundDuration(), createdLobby.getRoundDuration());
        assertEquals(testLobby.getCurrentLetter(), createdLobby.getCurrentLetter());
        assertEquals(testLobby.getCategoryCitiesActive(), createdLobby.getCategoryCitiesActive());
        assertEquals(testLobby.getCategoryRiversActive(), createdLobby.getCategoryRiversActive());
        assertEquals(testLobby.getCategoryCountriesActive(), createdLobby.getCategoryCountriesActive());
        assertEquals(testLobby.getCategoryMunicipalityActive(), createdLobby.getCategoryMunicipalityActive());
        assertEquals(testLobby.getRoundStarted(), createdLobby.getRoundStarted());
        assertNotNull(createdLobby.getHostToken());
    }
@Test
    void getLobbies_validInputs_success() {
        // given
        assertTrue(lobbyRepository.findAll().isEmpty());

        Lobby testLobby = new Lobby();
        testLobby.setHost("TestHost");
        testLobby.setRounds(1);
        testLobby.setHostToken("1234");
        testLobby.setCurrentLetter('a');
        testLobby.setRoundDuration(1);
        testLobby.setRand(new Random());
        testLobby.setRoundStarted(true);
        testLobby.setCategoryCitiesActive(true);
        testLobby.setCategoryCountriesActive(true);
        testLobby.setCategoryMunicipalityActive(false);
        testLobby.setCategoryRiversActive(false);
        List<Character> list = new ArrayList<>();
        list.add('a');
        list.add('b');
        testLobby.setUsedLetters(list);


        lobbyService.createLobby(testLobby);
        lobbyService.saveLobby(testLobby);
        // when
        System.out.println(lobbyService.getLobbies());
        System.out.println(lobbyRepository.findAll());

    // then
        assertThat(lobbyService.getLobbies(), samePropertyValuesAs(lobbyRepository.findAll()));

}
}

