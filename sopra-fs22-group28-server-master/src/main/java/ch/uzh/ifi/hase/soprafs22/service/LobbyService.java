package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.ErrorMSG;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
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
public class LobbyService {
    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }


    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    public Lobby getLobbyById(Long id) {
        Optional<Lobby> optionalLobby = lobbyRepository.findById(id);
        Lobby lobby = null;
        if (optionalLobby.isPresent()) {
            lobby = optionalLobby.get();
        }
        return lobby;
    }

    public Lobby saveLobby(Lobby toSaveLobby) {
        Lobby newLobby = lobbyRepository.save(toSaveLobby);
        lobbyRepository.flush();
        log.debug("Updated Information for Lobby: {}", newLobby);
        return newLobby;
    }

    public Lobby createLobby(Lobby newLobby) {
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();
        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }

    public Character pollLetterOfLobby(Lobby lobby) {
        if (lobby.getCurrentLetter() != null) {
            lobby.getUsedLetters().add(lobby.getCurrentLetter());
        }
        lobby.setCurrentLetter(lobby.getRandomChar());
        while (lobby.getUsedLetters().contains(lobby.getCurrentLetter())) {
            lobby.setCurrentLetter(lobby.getRandomChar());
        }
        lobbyRepository.flush();
        return lobby.getCurrentLetter();
    }
    public String startRound(Integer lobbyId) {
        Lobby newLobby = lobbyRepository.findById(lobbyId);
        if (newLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);}
        else{

            if (Boolean.TRUE.equals(newLobby.getRoundStarted())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.ROUND_STARTED_ALREADY);
            }
            else{
                newLobby.setRoundStarted(true);
                return "{'nextLetter':" + newLobby.getRandomChar() + "}";
            }
        }
        }
    public Character getCurrentLetterOfLobbyByLobbyId(Integer lobbyId){
        Lobby newLobby = lobbyRepository.findById(lobbyId);
        if (newLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);}
        else{
            if (newLobby.getCurrentLetter() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMSG.ROUND_NOT_STARTED_YET);
            }
            return newLobby.getCurrentLetter();

    }
}
    public Lobby startLobbyRound(Integer lobbyId) {
        Lobby newLobby = lobbyRepository.findById(lobbyId);
        if (newLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);}
        else{
            if (Boolean.TRUE.equals(newLobby.getRoundStarted())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMSG.CANNOT_CHANGE_SETTINGS_AFTER_GAME_START);
            }
            else{
                newLobby.setRoundStarted(true);
                return newLobby;
            }
        }
    }
    public String getNextRoundByLobbyID(Integer lobbyId) {
        Lobby newLobby = lobbyRepository.findById(lobbyId);
        if (newLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);}
        else{
            if (newLobby.getCurrentLetter()== null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.ROUND_NOT_STARTED_YET);
            }
            else{
                if (newLobby.getUsedLetters().size() > 25) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.LOBBY_ALPHABET_USED_UP);
                }
                return "{'nextLetter':" + newLobby.getRandomChar() + "}";
            }
        }
    }
    public String getNextLetterLobbyId(Integer lobbyId) {
        Lobby newLobby = lobbyRepository.findById(lobbyId);
        if (newLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);}
        else {
            if (newLobby.getRoundStarted()== null) {
                newLobby.setRoundStarted(false);
            }
            return "{'gameRunning':" + newLobby.getRoundStarted() + "}";
    }
}}
