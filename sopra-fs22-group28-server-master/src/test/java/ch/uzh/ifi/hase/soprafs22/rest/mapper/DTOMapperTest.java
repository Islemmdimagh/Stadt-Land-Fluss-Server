package ch.uzh.ifi.hase.soprafs22.rest.mapper;


import ch.uzh.ifi.hase.soprafs22.entity.Answer;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
class DTOMapperTest {
    @Test
    void testCreateLobby_fromLobbyPostDTO_toLobby_success() {
        // create LobbyPostDTO
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setHost("Bambo");
        lobbyPostDTO.setRoundDuration(2);
        lobbyPostDTO.setRounds(2);
        lobbyPostDTO.setCategoryCitiesActive(true);
        lobbyPostDTO.setCategoryCountriesActive(false);
        lobbyPostDTO.setCategoryRiversActive(true);
        lobbyPostDTO.setCategoryMunicipalityActive(false);
        lobbyPostDTO.setCustomCategory1("brand");
        lobbyPostDTO.setCustomCategory2("name");
        lobbyPostDTO.setCustomCategory3("animal");

        // MAP -> Create lobby
        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        // check content
        assertEquals(lobbyPostDTO.getHost(), lobby.getHost());
        assertEquals(lobbyPostDTO.getRoundDuration(), lobby.getRoundDuration());
        assertEquals(lobbyPostDTO.getRounds(), lobby.getRounds());
        assertEquals(lobbyPostDTO.getCategoryCitiesActive(), lobby.getCategoryCitiesActive());
        assertEquals(lobbyPostDTO.getCategoryCountriesActive(), lobby.getCategoryCountriesActive());
        assertEquals(lobbyPostDTO.getCategoryRiversActive(), lobby.getCategoryRiversActive());
        assertEquals(lobbyPostDTO.getCategoryMunicipalityActive(), lobby.getCategoryMunicipalityActive());
        assertEquals(lobbyPostDTO.getCustomCategory1(), lobby.getCustomCategory1());
        assertEquals(lobbyPostDTO.getCustomCategory2(), lobby.getCustomCategory2());
        assertEquals(lobbyPostDTO.getCustomCategory3(), lobby.getCustomCategory3());

    }

    @Test
    void testGetLobby_fromLobby_toLobbyGetDTO_success() {
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        lobby.setCurrentLetter('d');

        // MAP -> Create LobbyGetDTO
        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        // check content
        assertEquals(lobby.getHost(), lobbyGetDTO.getHost());
        assertEquals(lobby.getRoundDuration(), lobbyGetDTO.getRoundDuration());
        assertEquals(lobby.getRounds(), lobbyGetDTO.getRoundDuration());
        assertEquals(lobby.getCurrentLetter(), lobbyGetDTO.getCurrentLetter());
        assertEquals(lobby.getCategoryCitiesActive(), lobbyGetDTO.getCategoryCitiesActive());
        assertEquals(lobby.getCategoryCountriesActive(), lobbyGetDTO.getCategoryCountriesActive());
        assertEquals(lobby.getCategoryMunicipalityActive(), lobbyGetDTO.getCategoryMunicipalityActive());
        assertEquals(lobby.getCategoryRiversActive(), lobbyGetDTO.getCategoryRiversActive());
        assertEquals(lobby.getCustomCategory1(), lobbyGetDTO.getCustomCategory1());
        assertEquals(lobby.getCustomCategory2(), lobbyGetDTO.getCustomCategory2());
        assertEquals(lobby.getCustomCategory3(), lobbyGetDTO.getCustomCategory3());
    }

    @Test
    void testGetLobbyHost_fromLobbyHost_toLobbyHostGetDTO_success() {
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setHost("Bambo");
        lobby.setHostToken("123");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        lobby.setCurrentLetter('d');

        // MAP -> Create LobbyGetDTO
        LobbyHostGetDTO lobbyHostGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyHostGetDTO(lobby);

        // check content
        assertEquals(lobby.getHost(), lobbyHostGetDTO.getHost());
        assertEquals(lobby.getHostToken(), lobbyHostGetDTO.getHostToken());
        assertEquals(lobby.getRoundDuration(), lobbyHostGetDTO.getRoundDuration());
        assertEquals(lobby.getRounds(), lobbyHostGetDTO.getRoundDuration());
        assertEquals(lobby.getCurrentLetter(), lobbyHostGetDTO.getCurrentLetter());
        assertEquals(lobby.getCategoryCitiesActive(), lobbyHostGetDTO.getCategoryCitiesActive());
        assertEquals(lobby.getCategoryCountriesActive(), lobbyHostGetDTO.getCategoryCountriesActive());
        assertEquals(lobby.getCategoryMunicipalityActive(), lobbyHostGetDTO.getCategoryMunicipalityActive());
        assertEquals(lobby.getCategoryRiversActive(), lobbyHostGetDTO.getCategoryRiversActive());
        assertEquals(lobby.getCustomCategory1(), lobbyHostGetDTO.getCustomCategory1());
        assertEquals(lobby.getCustomCategory2(), lobbyHostGetDTO.getCustomCategory2());
        assertEquals(lobby.getCustomCategory3(), lobbyHostGetDTO.getCustomCategory3());
    }

    @Test
    void testCreateLobby_fromLobbyStartDTO_toLobby_success() {
        // create LobbyPostDTO
        LobbyStartDTO lobbyStartDTO = new LobbyStartDTO();
        lobbyStartDTO.setHostToken("12345");
        lobbyStartDTO.setRoundStarted(true);

        // MAP -> Create lobby
        Lobby lobby = DTOMapper.INSTANCE.convertLobbyStartDTOtoEntity(lobbyStartDTO);

        // check content
        assertEquals(lobbyStartDTO.getHostToken(), lobby.getHostToken());
        assertEquals(lobbyStartDTO.getRoundStarted(), lobby.getRoundStarted());
    }

    @Test
    void testCreateLobby_fromLobbyPatchDTO_toLobby_success() {
        // create LobbyPostDTO
        LobbyPatchDTO lobbyPatchDTO = new LobbyPatchDTO();
        lobbyPatchDTO.setHostToken("12345");
        lobbyPatchDTO.setRounds(2);
        lobbyPatchDTO.setRoundDuration(1);
        lobbyPatchDTO.setCategoryCitiesActive(true);
        lobbyPatchDTO.setCategoryCountriesActive(true);
        lobbyPatchDTO.setCategoryMunicipalityActive(false);
        lobbyPatchDTO.setCategoryRiversActive(false);

        // MAP -> Create lobby
        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPatchDTOtoEntity(lobbyPatchDTO);

        // check content
        assertEquals(lobbyPatchDTO.getHostToken(), lobby.getHostToken());
        assertEquals(lobbyPatchDTO.getRounds(), lobby.getRounds());
        assertEquals(lobbyPatchDTO.getRoundDuration(), lobby.getRoundDuration());
        assertEquals(lobbyPatchDTO.getCategoryCitiesActive(), lobby.getCategoryCitiesActive());
        assertEquals(lobbyPatchDTO.getCategoryCountriesActive(), lobby.getCategoryCountriesActive());
        assertEquals(lobbyPatchDTO.getCategoryMunicipalityActive(), lobby.getCategoryMunicipalityActive());
        assertEquals(lobbyPatchDTO.getCategoryRiversActive(), lobby.getCategoryRiversActive());
    }

    @Test
    void testCreatePlayer_fromPlayerPostDTO_toPlayer_success() {
        // create LobbyPostDTO
        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setName("Bambo");
        playerPostDTO.setLobbyId(2);

        // MAP -> Create lobby
        Player player = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);

        // check content
        assertEquals(playerPostDTO.getName(), player.getName());
        assertEquals(playerPostDTO.getLobbyId(), player.getLobbyId());
    }

    @Test
    void testGetPlayer_fromPlayer_toPlayerGetDTO_success() {
        // create Lobby
        Player player = new Player();
        player.setName("Peter");


        // MAP -> Create LobbyGetDTO
        PlayerGetDTO playerGetDTO = DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player);

        // check content
        assertEquals(player.getName(), playerGetDTO.getName());

    }

    @Test
    void testGetPlayer_fromPlayer_toPlayerGetWithTokenDTO_success() {
        // create Player
        Player player = new Player();
        player.setName("Peter");
        player.setPlayerToken("12345");


        // MAP -> Create LobbyGetDTO
        PlayerGetWithTokenDTO playerGetWithTokenDTO = DTOMapper.INSTANCE.convertEntityToPlayerGetWithTokenDTO(player);

        // check content
        assertEquals(player.getName(), playerGetWithTokenDTO.getName());
        assertEquals(player.getPlayerToken(), playerGetWithTokenDTO.getPlayerToken());

    }

    @Test
    void testGetAnswer_fromAnswer_toAnswerGetDTO_success() {
        // create Answer
        Answer answer = new Answer();
        answer.setAnswer("Peter");
        answer.setLetter('a');
        answer.setValid(true);
        answer.setPlayerID(1L);


        // MAP -> Create LobbyGetDTO
        AnswerGetDTO answerGetDTO = DTOMapper.INSTANCE.convertEntityToAnswerGetDTO(answer);

        // check content
        assertEquals(answer.getAnswer(), answerGetDTO.getAnswer());
        assertEquals(answer.getLetter(), answerGetDTO.getLetter());
        assertEquals(answer.getValid(), answerGetDTO.getValid());
        assertEquals(answer.getPlayerID(), answerGetDTO.getPlayerID());
    }

    @Test
    void testCreateAnswer_fromAnswerPostDTO_toAnswer_success() {
        // create AnswerPostDTO
        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setAnswer("Bambo");
        answerPostDTO.setPlayerToken("1234");

        // MAP -> Create lobby
        Answer answer = DTOMapper.INSTANCE.convertAnswerPostDTOtoEntity(answerPostDTO);

        // check content
        assertEquals(answerPostDTO.getAnswer(), answer.getAnswer());
        assertEquals(answerPostDTO.getPlayerToken(), answer.getPlayerToken());
    }
}