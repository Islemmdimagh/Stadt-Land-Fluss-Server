package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs22.repository.PlayerRepository;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see PlayerService
 */

@WebAppConfiguration
@SpringBootTest
class PlayerServiceIntegrationTest {

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;
    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private LobbyService lobbyService;

    @BeforeEach
    public void setup() {
        playerRepository.deleteAll();
        lobbyRepository.deleteAll();
    }

    @AfterEach
    public void setup2() {
        playerRepository.deleteAll();
        lobbyRepository.deleteAll();
    }

    @Test
    void createPlayer_validInputs_success() {
        // given
        assertTrue(playerRepository.findAll().isEmpty());
        Lobby testLobby = new Lobby();
        testLobby.setHost("peter");
        testLobby.setRoundDuration(1);
        testLobby.setRounds(1);
        testLobby.setRand(new Random());
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        Player testPlayer = new Player();
        Long LobbyID = createdLobby.getId();
        testPlayer.setLobbyId(LobbyID.intValue());
        testPlayer.setName("Peter");

        // when
        Player createdPlayer = playerService.createPlayer(testPlayer);

        // then
        assertEquals(testPlayer.getId(), createdPlayer.getId());
        assertEquals(testPlayer.getPlayerToken(), createdPlayer.getPlayerToken());
        assertEquals(testPlayer.getLobbyId(), createdPlayer.getLobbyId());
        assertEquals(testPlayer.getName(), createdPlayer.getName());
        assertEquals(testPlayer.getLobby(), createdPlayer.getLobby());
    }

    @Test
    void getPlayerByPlayerToken_validInputs_success() {
        // given
        assertTrue(playerRepository.findAll().isEmpty());
        Lobby testLobby = new Lobby();
        testLobby.setHost("peter");
        testLobby.setRoundDuration(1);
        testLobby.setRounds(1);
        testLobby.setRand(new Random());
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        Player testPlayer = new Player();
        Long LOBBYID = createdLobby.getId();
        testPlayer.setLobbyId(LOBBYID.intValue());
        testPlayer.setName("Peter");
        playerService.createPlayer(testPlayer);
        playerService.savePlayer(testPlayer);
        // when
        Player createdPlayer = playerService.getPlayerByPlayerToken(testPlayer.getPlayerToken());
        // then
        assertEquals(testPlayer.getId(), createdPlayer.getId());
        assertEquals(testPlayer.getPlayerToken(), createdPlayer.getPlayerToken());
        assertEquals(testPlayer.getLobbyId(), createdPlayer.getLobbyId());
        assertEquals(testPlayer.getName(), createdPlayer.getName());
    }

    @Test
    void getPlayersByLobbyId_validInputs_success() {
        // given
        assertTrue(playerRepository.findAll().isEmpty());

        Lobby testLobby = new Lobby();
        testLobby.setHost("TestHost");
        testLobby.setId(1L);
        testLobby.setRounds(1);
        testLobby.setHostToken("1234");
        testLobby.setCurrentLetter('a');
        testLobby.setRoundDuration(1);
        testLobby.setRand(new Random());
        testLobby.setRoundStarted(false);
        testLobby.setCategoryCitiesActive(true);
        testLobby.setCategoryCountriesActive(true);
        testLobby.setCategoryMunicipalityActive(false);
        testLobby.setCategoryRiversActive(false);
        List<Character> list = new ArrayList<>();
        list.add('a');
        list.add('b');
        testLobby.setUsedLetters(list);
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        Player player = new Player();
        Long LOBBYID = createdLobby.getId();
        player.setLobbyId(LOBBYID.intValue());
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setPoints(2);
        player.setLobby(testLobby);
        List<Player> players = new ArrayList<>();
        Player createdplayer = playerService.createPlayer(player);
        players.add(player);
        testLobby.setPlayers(players);


        // then
        Player player1 = (playerService.getPlayersByLobbyID(player.getLobbyId())).get(0);
        Player player2 = testLobby.getPlayers().get(0);

        assertEquals(player1.getName(), player2.getName());
        assertEquals(player1.getPlayerToken(), player2.getPlayerToken());
        assertEquals(player1.getLobbyId(), player2.getLobbyId());
        assertEquals(player1.getCurrentAnswers(), player2.getCurrentAnswers());
        assertEquals(player1.getPoints(), player2.getPoints());

    }
    @Test
    void getPlayersByLobbyId_invalidInput_NoLobbyExists_success() {
        // given
        assertTrue(playerRepository.findAll().isEmpty());

        Lobby testLobby = new Lobby();
        testLobby.setHost("TestHost");
        testLobby.setId(1L);
        testLobby.setRounds(1);
        testLobby.setHostToken("1234");
        testLobby.setCurrentLetter('a');
        testLobby.setRoundDuration(1);
        testLobby.setRand(new Random());
        testLobby.setRoundStarted(false);
        testLobby.setCategoryCitiesActive(true);
        testLobby.setCategoryCountriesActive(true);
        testLobby.setCategoryMunicipalityActive(false);
        testLobby.setCategoryRiversActive(false);
        List<Character> list = new ArrayList<>();
        list.add('a');
        list.add('b');
        testLobby.setUsedLetters(list);
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        Player player = new Player();
        Long LOBBYID = createdLobby.getId();
        player.setLobbyId(LOBBYID.intValue());
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setPoints(2);
        playerService.createPlayer(player);
        List<Player> players = new ArrayList<>();
        players.add(player);
        testLobby.setPlayers(players);


        // then

        assertThrows(ResponseStatusException.class, () -> playerService.getPlayersByLobbyID(2));


    }
}