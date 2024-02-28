package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.ErrorMSG;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import ch.uzh.ifi.hase.soprafs22.repository.AnswerRepository;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs22.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlayerService {
    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository, LobbyRepository lobbyRepository) {
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public Player savePlayer(Player toSavePlayer) {
        Player newPlayer = playerRepository.save(toSavePlayer);
        playerRepository.flush();
        log.debug("Updated Information for Player: {}", newPlayer);
        return newPlayer;
    }


    public Player getPlayerByPlayerToken(String token) {
        Optional<Player> optionalPlayer = Optional.ofNullable(playerRepository.findByPlayerToken(token));
        Player player = null;
        if (optionalPlayer.isPresent()) {
            player = optionalPlayer.get();
        }
        return player;
    }


    public Player createPlayer(Player newPlayer) {
        checkIfPlayerExists(newPlayer);
        checkIfLobbyExists(newPlayer.getLobbyId());
        checkIfLobbyClosed(newPlayer.getLobbyId());
        newPlayer.setLobby(lobbyRepository.findById(newPlayer.getLobbyId()));
        newPlayer = playerRepository.save(newPlayer);
        playerRepository.flush();
        log.debug("Created Information for Player: {}", newPlayer);
        return newPlayer;
    }

    public List<Player> getPlayersByLobbyID(Integer lobbyID) {
        Lobby lobbyById = lobbyRepository.findById(lobbyID);
        if (lobbyById == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        return lobbyById.getPlayers();

    }

    private void checkIfLobbyExists(Integer lobbyId) {
        Lobby lobbyById = lobbyRepository.findById(lobbyId);
        if (lobbyById == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
    }

    private void checkIfLobbyClosed(Integer lobbyId) {
        Lobby lobbyById = lobbyRepository.findById(lobbyId);
        if (Boolean.TRUE.equals(lobbyById.getRoundStarted())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.ROUND_STARTED_ALREADY);
        }
    }

    private void checkIfPlayerExists(Player playerToBeCreated) {

        Player playerByUsername = playerRepository.findByName(playerToBeCreated.getName());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the player could not be created!";

        if (playerByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }
    }


}
