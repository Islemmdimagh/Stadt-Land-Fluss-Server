package ch.uzh.ifi.hase.soprafs22.controller;
import ch.uzh.ifi.hase.soprafs22.constant.ErrorMSG;
import ch.uzh.ifi.hase.soprafs22.entity.Answer;
import ch.uzh.ifi.hase.soprafs22.entity.ValidationResult;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import ch.uzh.ifi.hase.soprafs22.service.AnswerService;
import ch.uzh.ifi.hase.soprafs22.service.LobbyService;
import ch.uzh.ifi.hase.soprafs22.service.PlayerService;
import ch.uzh.ifi.hase.soprafs22.utils.Validator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * LobbyControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */

@WebMvcTest(LobbyController.class)
class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;
    @MockBean
    private PlayerService playerService;
    @MockBean
    private AnswerService answerService;

    @Test
    void givenLobby_whenGetLobbyById_thenReturnJsonArray() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
        System.out.println(lobby);
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


        //List<User> allUsers = Collections.singletonList(lobby);

        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        lobbyService.saveLobby(lobby);
        System.out.println(lobbyService.getLobbies());

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);

        // when

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lobby.getId())))
                .andExpect(jsonPath("$.host", is(lobby.getHost())))
                .andExpect(jsonPath("$.rounds", is(lobby.getRounds())))
                .andExpect(jsonPath("$.customCategory3", is(lobby.getCustomCategory3())))
                .andExpect(jsonPath("$.roundDuration", is(lobby.getRoundDuration())))
                .andExpect(jsonPath("$.categoryCitiesActive", is(lobby.getCategoryCitiesActive())))
                .andExpect(jsonPath("$.categoryCountriesActive", is(lobby.getCategoryCountriesActive())))
                .andExpect(jsonPath("$.categoryRiversActive", is(lobby.getCategoryRiversActive())))
                .andExpect(jsonPath("$.categoryMunicipalityActive", is(lobby.getCategoryMunicipalityActive())))
                .andExpect(jsonPath("$.customCategory1", is(lobby.getCustomCategory1())))
                .andExpect(jsonPath("$.customCategory2", is(lobby.getCustomCategory2())));

    }
    @Test
    void getPlayersByLobbyID_validInput() throws Exception {
        // given

        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
        System.out.println(lobby);
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
        lobbyService.saveLobby(lobby);

        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setLobby(lobby);
        player.setPoints(2);

        // when

        List<Player> allPlayers = Collections.singletonList(player);

        lobby.setPlayers(allPlayers);

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(playerService.getPlayersByLobbyID(Mockito.any())).willReturn(allPlayers);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/users")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk());
    }
    @Test
    void getPlayersByLobbyId_invalidLobbyID() throws Exception {
        // given

        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
        System.out.println(lobby);
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
        lobbyService.saveLobby(lobby);

        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setLobby(lobby);

        // when

        List<Player> allPlayers = Collections.singletonList(player);

        lobby.setPlayers(allPlayers);

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(playerService.getPlayersByLobbyID(Mockito.any())).willReturn(null);
        // when

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/users").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());

    }
    @Test
    void createPlayer_validInput_playerCreated() throws Exception {
        // given
        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);


        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setName("Test User");
        playerPostDTO.setLobbyId(1);

        given(playerService.createPlayer(Mockito.any())).willReturn(player);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(player.getId().intValue())))
                .andExpect(jsonPath("$.name", is(player.getName())))
                .andExpect(jsonPath("$.playerToken", is(player.getPlayerToken())));
    }

    @Test
    void createLobby_validInput_lobbyCreated() throws Exception {
        // given
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

        given(lobbyService.createLobby(Mockito.any())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.host", is(lobby.getHost())))
                .andExpect(jsonPath("$.rounds", is(lobby.getRounds())))
                .andExpect(jsonPath("$.customCategory3", is(lobby.getCustomCategory3())))
                .andExpect(jsonPath("$.roundDuration", is(lobby.getRoundDuration())))
                .andExpect(jsonPath("$.categoryCitiesActive", is(lobby.getCategoryCitiesActive())))
                .andExpect(jsonPath("$.categoryCountriesActive", is(lobby.getCategoryCountriesActive())))
                .andExpect(jsonPath("$.categoryRiversActive", is(lobby.getCategoryRiversActive())))
                .andExpect(jsonPath("$.categoryMunicipalityActive", is(lobby.getCategoryMunicipalityActive())))
                .andExpect(jsonPath("$.customCategory1", is(lobby.getCustomCategory1())))
                .andExpect(jsonPath("$.customCategory2", is(lobby.getCustomCategory2())));

    }    @Test
    void createLobby_invalidInput_lobbyNotCreated() throws Exception {
        // given
        Lobby lobby = new Lobby();

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

        given(lobbyService.createLobby(Mockito.any())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isInternalServerError());

    }
    @Test
    void getLobbyByID_invalidLobbyID() throws Exception {

        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
        lobby.setId(2L);
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

        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST))
                .when(lobbyService).getLobbyById(null);

        // when

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());

    }
    @Test
    void startRound_validInput_RoundStarted() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");


        LobbyStartDTO lobbyStartDTO = new LobbyStartDTO();
        lobbyStartDTO.setHostToken("12345");
        lobbyStartDTO.setRoundStarted(false);
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";

        given(lobbyService.pollLetterOfLobby(Mockito.any())).willReturn(lobby.getCurrentLetter());
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.startRound(Mockito.any())).willReturn(content);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/startRound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyStartDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk());

    }
    @Test
    void startRound_invalidInput_NoLobbyFound() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");


        LobbyStartDTO lobbyStartDTO = new LobbyStartDTO();
        lobbyStartDTO.setHostToken("12345");
        lobbyStartDTO.setRoundStarted(false);
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";

        given(lobbyService.pollLetterOfLobby(Mockito.any())).willReturn(lobby.getCurrentLetter());
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(null);
        given(lobbyService.startRound(Mockito.any())).willReturn(content);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/startRound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyStartDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());

    }
    @Test
    void startRound_invalidInput_wrongToken() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");


        LobbyStartDTO lobbyStartDTO = new LobbyStartDTO();
        lobbyStartDTO.setHostToken("123456");
        lobbyStartDTO.setRoundStarted(false);
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";

        given(lobbyService.pollLetterOfLobby(Mockito.any())).willReturn(lobby.getCurrentLetter());
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.startRound(Mockito.any())).willReturn(content);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/startRound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyStartDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isUnauthorized());

    }
    @Test
    void startRound_invalidInput_LobbyAlreadyStarted() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        lobby.setRoundStarted(true);


        LobbyStartDTO lobbyStartDTO = new LobbyStartDTO();
        lobbyStartDTO.setHostToken("12345");
        lobbyStartDTO.setRoundStarted(false);
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";

        given(lobbyService.pollLetterOfLobby(Mockito.any())).willReturn(lobby.getCurrentLetter());
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.startRound(Mockito.any())).willReturn(content);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/startRound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyStartDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }
    @Test
    void getCurrentLetterOfLobby_ByLobbyId_validInput() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');

        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        System.out.println(lobby);

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.getCurrentLetterOfLobbyByLobbyId(Mockito.any())).willReturn(lobby.getCurrentLetter());


        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/letter").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk());
    }    @Test
    void getCurrentLetterOfLobby_ByLobbyId_invalidInput_noCurrentLetter() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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


        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        System.out.println(lobby);

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.getCurrentLetterOfLobbyByLobbyId(Mockito.any())).willReturn(lobby.getCurrentLetter());


        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/letter").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isBadRequest());
    }
    @Test
    void getCurrentLetterOfLobby_ByLobbyId_invalidInput_noLobbyFound() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');

        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        System.out.println(lobby);


        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST))
                .when(lobbyService).getLobbyById(null);
        given(lobbyService.getCurrentLetterOfLobbyByLobbyId(Mockito.any())).willReturn(lobby.getCurrentLetter());

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/letter").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }
    @Test
    void startLobbyRound_validInput_RoundStarted() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        lobby.setRoundStarted(false);

        LobbyPatchDTO lobbyPatchDTO = new LobbyPatchDTO();
        lobbyPatchDTO.setHostToken("12345");
        lobbyPatchDTO.setRounds(1);
        lobbyPatchDTO.setRoundDuration(10);
        lobbyPatchDTO.setCategoryRiversActive(true);
        lobbyPatchDTO.setCategoryMunicipalityActive(false);
        lobbyPatchDTO.setCategoryCountriesActive(true);
        lobbyPatchDTO.setCategoryCitiesActive(false);

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.startLobbyRound(Mockito.any())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder patchrequest = patch("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPatchDTO));

        // then
        mockMvc.perform(patchrequest)
                .andExpect(status().isOk());

    }
    @Test
    void startLobbyRound_invalidInput_RoundStartedAlready() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        lobby.setRoundStarted(true);

        LobbyPatchDTO lobbyPatchDTO = new LobbyPatchDTO();
        lobbyPatchDTO.setHostToken("12345");
        lobbyPatchDTO.setRounds(1);
        lobbyPatchDTO.setRoundDuration(10);
        lobbyPatchDTO.setCategoryRiversActive(true);
        lobbyPatchDTO.setCategoryMunicipalityActive(false);
        lobbyPatchDTO.setCategoryCountriesActive(true);
        lobbyPatchDTO.setCategoryCitiesActive(false);

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.startLobbyRound(Mockito.any())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder patchrequest = patch("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPatchDTO));

        // then
        mockMvc.perform(patchrequest)
                .andExpect(status().isBadRequest());

    }@Test
    void startLobbyRound_invalidInput_WrongHostToken() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        lobby.setRoundStarted(true);

        LobbyPatchDTO lobbyPatchDTO = new LobbyPatchDTO();
        lobbyPatchDTO.setHostToken("1234");
        lobbyPatchDTO.setRounds(1);
        lobbyPatchDTO.setRoundDuration(10);
        lobbyPatchDTO.setCategoryRiversActive(true);
        lobbyPatchDTO.setCategoryMunicipalityActive(false);
        lobbyPatchDTO.setCategoryCountriesActive(true);
        lobbyPatchDTO.setCategoryCitiesActive(false);

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.startLobbyRound(Mockito.any())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder patchrequest = patch("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPatchDTO));

        // then
        mockMvc.perform(patchrequest)
                .andExpect(status().isUnauthorized());

    }
    @Test
    void startLobbyRound_invalidInput_LobbyNotFound() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        lobby.setRoundStarted(false);

        LobbyPatchDTO lobbyPatchDTO = new LobbyPatchDTO();
        lobbyPatchDTO.setHostToken("12345");
        lobbyPatchDTO.setRounds(1);
        lobbyPatchDTO.setRoundDuration(10);
        lobbyPatchDTO.setCategoryRiversActive(true);
        lobbyPatchDTO.setCategoryMunicipalityActive(false);
        lobbyPatchDTO.setCategoryCountriesActive(true);
        lobbyPatchDTO.setCategoryCitiesActive(false);

        given(lobbyService.startLobbyRound(Mockito.any())).willReturn(lobby);
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST))
                .when(lobbyService).getLobbyById(null);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder patchrequest = patch("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPatchDTO));

        // then
        mockMvc.perform(patchrequest)
                .andExpect(status().isNotFound());

    }
    @Test
    void getNextRoundByLobbyId_validInput_UsedLettersSameOrMoreThanRounds() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        List<Character> usedLetters = new ArrayList<>();
        usedLetters.add('a');
        usedLetters.add('c');
        lobby.setUsedLetters(usedLetters);


        LobbyNextRoundDTO lobbyNextRoundDTO = new LobbyNextRoundDTO();
        lobbyNextRoundDTO.setHostToken("12345");
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";

        given(lobbyService.pollLetterOfLobby(Mockito.any())).willReturn(lobby.getCurrentLetter());
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.getNextRoundByLobbyID(Mockito.any())).willReturn(content);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/nextRound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyNextRoundDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk());

    }@Test
    void getNextRoundByLobbyId_validInput_UsedLettersLessThanRounds() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        List<Character> usedLetters = new ArrayList<>();
        usedLetters.add('a');
        lobby.setUsedLetters(usedLetters);


        LobbyNextRoundDTO lobbyNextRoundDTO = new LobbyNextRoundDTO();
        lobbyNextRoundDTO.setHostToken("12345");
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";

        given(lobbyService.pollLetterOfLobby(Mockito.any())).willReturn(lobby.getCurrentLetter());
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.getNextRoundByLobbyID(Mockito.any())).willReturn(content);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/nextRound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyNextRoundDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk());

    }@Test
    void getNextRoundByLobbyId_invalidInput_LobbyNotExists() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        List<Character> usedLetters = new ArrayList<>();
        usedLetters.add('a');
        lobby.setUsedLetters(usedLetters);


        LobbyNextRoundDTO lobbyNextRoundDTO = new LobbyNextRoundDTO();
        lobbyNextRoundDTO.setHostToken("12345");
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST))
                .when(lobbyService).getLobbyById(null);

        given(lobbyService.pollLetterOfLobby(Mockito.any())).willReturn(lobby.getCurrentLetter());
        given(lobbyService.getNextRoundByLobbyID(Mockito.any())).willReturn(content);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/nextRound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyNextRoundDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());

    }
    @Test
    void getNextRoundByLobbyId_invalidInput_WrongHostToken() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        List<Character> usedLetters = new ArrayList<>();
        usedLetters.add('a');
        lobby.setUsedLetters(usedLetters);


        LobbyNextRoundDTO lobbyNextRoundDTO = new LobbyNextRoundDTO();
        lobbyNextRoundDTO.setHostToken("1234");
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.pollLetterOfLobby(Mockito.any())).willReturn(lobby.getCurrentLetter());
        given(lobbyService.getNextRoundByLobbyID(Mockito.any())).willReturn(content);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/nextRound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyNextRoundDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isUnauthorized());

    }
    @Test
    void getNextRoundByLobbyId_invalidInput_RoundNotStartedYet() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        List<Character> usedLetters = new ArrayList<>();
        usedLetters.add('a');
        lobby.setUsedLetters(usedLetters);


        LobbyNextRoundDTO lobbyNextRoundDTO = new LobbyNextRoundDTO();
        lobbyNextRoundDTO.setHostToken("12345");
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.pollLetterOfLobby(Mockito.any())).willReturn(lobby.getCurrentLetter());
        given(lobbyService.getNextRoundByLobbyID(Mockito.any())).willReturn(content);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/nextRound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyNextRoundDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());

    }@Test
    void getNextRoundByLobbyId_invalidInput_AlphabetUsedUp() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        List<Character> usedLetters = new ArrayList<>();
        usedLetters.add('a');
        usedLetters.add('b');
        usedLetters.add('c');
        usedLetters.add('d');
        usedLetters.add('e');
        usedLetters.add('f');
        usedLetters.add('g');
        usedLetters.add('h');
        usedLetters.add('i');
        usedLetters.add('j');
        usedLetters.add('k');
        usedLetters.add('l');
        usedLetters.add('m');
        usedLetters.add('n');
        usedLetters.add('o');
        usedLetters.add('p');
        usedLetters.add('q');
        usedLetters.add('r');
        usedLetters.add('s');
        usedLetters.add('t');
        usedLetters.add('u');
        usedLetters.add('v');
        usedLetters.add('w');
        usedLetters.add('x');
        usedLetters.add('y');
        usedLetters.add('z');

        lobby.setUsedLetters(usedLetters);


        LobbyNextRoundDTO lobbyNextRoundDTO = new LobbyNextRoundDTO();
        lobbyNextRoundDTO.setHostToken("12345");
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.pollLetterOfLobby(Mockito.any())).willReturn(lobby.getCurrentLetter());
        given(lobbyService.getNextRoundByLobbyID(Mockito.any())).willReturn(content);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/nextRound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyNextRoundDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());

    }
    @Test
    void getNextLetterOfLobby_ByLobbyId_validInput() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(null);

        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        System.out.println(lobby);
        String content = "{'gameRunning':" + lobby.getRoundStarted() + "}";
        given(lobbyService.getLobbyById( Mockito.any())).willReturn(lobby);
        given(lobbyService.getNextLetterLobbyId(Mockito.any())).willReturn(content);


        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/gameRunning").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk());
    }
    @Test
    void getNextLetterOfLobby_ByLobbyId_invalidInput_NoLobbyExists() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(true);

        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        System.out.println(lobby);
        String content = "{'gameRunning':" + lobby.getRoundStarted() + "}";
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST))
                .when(lobbyService).getLobbyById(null);
        given(lobbyService.getNextLetterLobbyId(Mockito.any())).willReturn(content);


        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/gameRunning").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }
    @Test
    void getGameFinished_ByLobbyId_validInput() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(null);

        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        given(lobbyService.getLobbyById( Mockito.any())).willReturn(lobby);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/gameFinished").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$.gameFinished", is(lobby.getGameFinished())));
    }
    @Test
    void getGameFinished_ByLobbyId_InvalidInput_NoLobbyExists() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(null);

        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST))
                .when(lobbyService).getLobbyById(null);
        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/gameFinished").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }
    @Test
    void getSubmissionOpen_ByLobbyId_validInput_Null() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);
        lobby.setSubmissionOpen(null);


        given(lobbyService.getLobbyById( Mockito.any())).willReturn(lobby);
        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/submissionOpen").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionOpen", is(false)));
    }@Test
    void getSubmissionOpen_ByLobbyId_validInput_NotNull() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);


        given(lobbyService.getLobbyById( Mockito.any())).willReturn(lobby);
        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/submissionOpen").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionOpen", is(lobby.getSubmissionOpen())));
    }@Test
    void getSubmissionOpen_ByLobbyId_InvalidInput_NoLobbyExist() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);

        // when
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST))
                .when(lobbyService).getLobbyById(null);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/submissionOpen").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }
    @Test
    void checkSubmitAnswer_validInput() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);


        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);

        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setAnswer("Angola");
        answerPostDTO.setPlayerToken("1");
        answerPostDTO.setCategory(2);


        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);
        given(playerService.getPlayerByPlayerToken(Mockito.any())).willReturn(player);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/submitAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(answer.getId().intValue())))
                .andExpect(jsonPath("$.playerID", is(answer.getPlayerID().intValue())))
                .andExpect(jsonPath("$.letter", is(answer.getLetter().toString())))
                .andExpect(jsonPath("$.answer", is(answer.getAnswer())))
                .andExpect(jsonPath("$.valid", is(answer.getValid())))
                .andExpect(jsonPath("$.geoNameID", is(answer.getGeoNameID())))
                .andExpect(jsonPath("$.wikipediaLink", is(answer.getWikipediaLink())))
                .andExpect(jsonPath("$.points", is(answer.getPoints())));


    }    @Test
    void checkSubmitAnswer_validInput_ResultNull() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);


        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);

        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setAnswer("Angola");
        answerPostDTO.setPlayerToken("1");
        answerPostDTO.setCategory(7);


        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);
        given(playerService.getPlayerByPlayerToken(Mockito.any())).willReturn(player);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/submitAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(answer.getId().intValue())))
                .andExpect(jsonPath("$.playerID", is(answer.getPlayerID().intValue())))
                .andExpect(jsonPath("$.letter", is(answer.getLetter().toString())))
                .andExpect(jsonPath("$.answer", is(answer.getAnswer())))
                .andExpect(jsonPath("$.valid", is(answer.getValid())))
                .andExpect(jsonPath("$.geoNameID", is(answer.getGeoNameID())))
                .andExpect(jsonPath("$.wikipediaLink", is(answer.getWikipediaLink())))
                .andExpect(jsonPath("$.points", is(answer.getPoints())));


    }@Test
    void checkSubmitAnswer_invalidInput_NoLobbyExists() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);


        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);

        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setAnswer("Angola");
        answerPostDTO.setPlayerToken("1");
        answerPostDTO.setCategory(2);


        given(lobbyService.getLobbyById(Mockito.any())).willReturn(null);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);
        given(playerService.getPlayerByPlayerToken(Mockito.any())).willReturn(player);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/submitAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());


    }@Test
    void checkSubmitAnswer_invalidInput_NoGoodPlayerToken() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);


        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);

        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setAnswer("Angola");
        answerPostDTO.setPlayerToken("1");
        answerPostDTO.setCategory(2);


        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);
        given(playerService.getPlayerByPlayerToken(Mockito.any())).willReturn(null);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/submitAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());


    }@Test
    void checkSubmitAnswer_invalidInput_NoCurrentLetter() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setRoundStarted(false);


        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);

        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setAnswer("Angola");
        answerPostDTO.setPlayerToken("1");
        answerPostDTO.setCategory(2);


        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);
        given(playerService.getPlayerByPlayerToken(Mockito.any())).willReturn(player);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/submitAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());


    }
    @Test
    void getAnswersOfCurrentLetter_validInput() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
        System.out.println(lobby);
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
        lobby.setCurrentLetter('a');

        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);
        List<Player> players = new ArrayList<>();
        players.add(player);
        lobby.setPlayers(players);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);



        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        lobbyService.saveLobby(lobby);


        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);

        // when

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/answersOfCurrentLetter").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
                .andExpect(jsonPath("$.players", is(notNullValue())))
                .andExpect(jsonPath("$.answers", is(notNullValue())))
        ;
    }
    @Test
    void getAnswersOfCurrentLetter_invalidInput_NoLobbyExists() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
        System.out.println(lobby);
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
        lobby.setCurrentLetter('a');

        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);
        List<Player> players = new ArrayList<>();
        players.add(player);
        lobby.setPlayers(players);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);



        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        lobbyService.saveLobby(lobby);


        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(null);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);

        // when

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/answersOfCurrentLetter").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }
    @Test
    void getEndGameInfo_validInput() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
        System.out.println(lobby);
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
        lobby.setCurrentLetter('a');

        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);
        List<Player> players = new ArrayList<>();
        players.add(player);
        lobby.setPlayers(players);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);



        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        lobbyService.saveLobby(lobby);


        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);

        // when

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/endgame").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
                .andExpect(jsonPath("$.host", is(lobby.getHost())))
                .andExpect(jsonPath("$.rounds", is(lobby.getRounds())))
                .andExpect(jsonPath("$.roundStarted", is(lobby.getRoundStarted())))
                .andExpect(jsonPath("$.submissionOpen", is(lobby.getSubmissionOpen())))
                .andExpect(jsonPath("$.roundDuration", is(lobby.getRoundDuration())))
                .andExpect(jsonPath("$.categoryCitiesActive", is(lobby.getCategoryCitiesActive())))
                .andExpect(jsonPath("$.categoryCountriesActive", is(lobby.getCategoryCountriesActive())))
                .andExpect(jsonPath("$.categoryRiversActive", is(lobby.getCategoryRiversActive())))
                .andExpect(jsonPath("$.categoryMunicipalityActive", is(lobby.getCategoryMunicipalityActive())))
                .andExpect(jsonPath("$.customCategory1", is(lobby.getCustomCategory1())))
                .andExpect(jsonPath("$.customCategory2", is(lobby.getCustomCategory2())))
                .andExpect(jsonPath("$.customCategory3", is(lobby.getCustomCategory3())))
                .andExpect(jsonPath("$.currentLetter", is(lobby.getCurrentLetter().toString())))
                .andExpect(jsonPath("$.players", is(notNullValue())))
                .andExpect(jsonPath("$.answers", is(notNullValue())));
    }
    @Test
    void getEndGameInfo_invalidInput_NoLobbyExists() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
        System.out.println(lobby);
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
        lobby.setCurrentLetter('a');

        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);
        List<Player> players = new ArrayList<>();
        players.add(player);
        lobby.setPlayers(players);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);



        // this mocks the LobbyService -> we define above what the LobbyService should
        // return when getLobbies() is called
        lobbyService.saveLobby(lobby);


        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(null);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);

        // when

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/endgame").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
               }
    @Test
    void voteAnswer_validInput() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);


        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);


        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");

        List <String> playersThatVoted = new ArrayList<>();
        playersThatVoted.add(player.getId().toString());
        answer.setPlayersThatAlreadyVoted(playersThatVoted);

        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);

        AnswerVoteDTO answerVoteDTO = new AnswerVoteDTO();
        answerVoteDTO.setAnswer("Angola");
        answerVoteDTO.setPlayerToken("12");

        given(answerService.getAnswerById(Mockito.any())).willReturn(answer);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);
        given(playerService.getPlayerByPlayerToken(Mockito.any())).willReturn(player);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/answers/1/submitVote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerVoteDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(answer.getId().intValue())))
                .andExpect(jsonPath("$.playerID", is(answer.getPlayerID().intValue())))
                .andExpect(jsonPath("$.letter", is(answer.getLetter().toString())))
                .andExpect(jsonPath("$.answer", is(answer.getAnswer())))
                .andExpect(jsonPath("$.valid", is(answer.getValid())))
                .andExpect(jsonPath("$.geoNameID", is(answer.getGeoNameID())))
                .andExpect(jsonPath("$.wikipediaLink", is(answer.getWikipediaLink())))
                .andExpect(jsonPath("$.points", is(answer.getPoints())));


    }

    @Test
    void voteAnswer_invalidInput_NoAnswerExists() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);


        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);


        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");

        List<String> playersThatVoted = new ArrayList<>();
        playersThatVoted.add(player.getId().toString());
        answer.setPlayersThatAlreadyVoted(playersThatVoted);

        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);

        AnswerVoteDTO answerVoteDTO = new AnswerVoteDTO();
        answerVoteDTO.setAnswer("Angola");
        answerVoteDTO.setPlayerToken("12");

        given(answerService.getAnswerById(Mockito.any())).willReturn(null);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);
        given(playerService.getPlayerByPlayerToken(Mockito.any())).willReturn(player);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/answers/1/submitVote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerVoteDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }
    @Test
    void voteAnswer_invalidInput_NoGoodPlayerTokenExists() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);


        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);


        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");

        List <String> playersThatVoted = new ArrayList<>();
        playersThatVoted.add(player.getId().toString());
        answer.setPlayersThatAlreadyVoted(playersThatVoted);

        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);

        AnswerVoteDTO answerVoteDTO = new AnswerVoteDTO();
        answerVoteDTO.setAnswer("Angola");
        answerVoteDTO.setPlayerToken("12");

        given(answerService.getAnswerById(Mockito.any())).willReturn(answer);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);
        given(playerService.getPlayerByPlayerToken(Mockito.any())).willReturn(null);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/answers/1/submitVote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerVoteDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());


    }@Test
    void voteAnswer_invalidInput_PlayerAlreadyVoted() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobbyService.createLobby(lobby);
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
        lobby.setCurrentLetter('a');
        lobby.setRoundStarted(false);


        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");

        List <String> playersThatVoted = new ArrayList<>();
        playersThatVoted.add(player.getId().toString());
        answer.setPlayersThatAlreadyVoted(playersThatVoted);

        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);

        AnswerVoteDTO answerVoteDTO = new AnswerVoteDTO();
        answerVoteDTO.setAnswer("Angola");
        answerVoteDTO.setPlayerToken("1");

        given(answerService.getAnswerById(Mockito.any())).willReturn(answer);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);
        given(playerService.getPlayerByPlayerToken(Mockito.any())).willReturn(player);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/answers/1/submitVote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerVoteDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());


    }
    @Test
    void getAllAnswersGroupedByLetter_validInput() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        List<Character> usedLetters = new ArrayList<>();
        usedLetters.add('a');
        usedLetters.add('c');
        lobby.setUsedLetters(usedLetters);

        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);
        List<Player> players = new ArrayList<>();
        players.add(player);
        lobby.setPlayers(players);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");

        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);


        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/answersGroupedByLetter").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());

    }
    @Test
    void getAllAnswersGroupedByLetter_invalidInput_NoLobbyExists() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setHost("Bambo");
        lobby.setHostToken("12345");
        lobby.setRoundDuration(2);
        lobby.setRounds(2);
        lobby.setCurrentLetter('a');
        lobby.setCategoryCitiesActive(true);
        lobby.setCategoryCountriesActive(false);
        lobby.setCategoryRiversActive(true);
        lobby.setCategoryMunicipalityActive(false);
        lobby.setCustomCategory1("brand");
        lobby.setCustomCategory2("name");
        lobby.setCustomCategory3("animal");
        List<Character> usedLetters = new ArrayList<>();
        usedLetters.add('a');
        usedLetters.add('c');
        lobby.setUsedLetters(usedLetters);

        Player player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setPlayerToken("1");
        player.setLobbyId(1);
        player.setPoints(2);
        List<Player> players = new ArrayList<>();
        players.add(player);
        lobby.setPlayers(players);

        Answer answer = new Answer();
        answer.setAnswer("Angola");
        answer.setPlayerID(1L);
        answer.setLobby(lobby);
        answer.setPoints(1);
        answer.setCategory(2);
        answer.setLetter('a');
        answer.setId(1L);
        answer.setValid(true);
        answer.setGeoNameID("123");
        answer.setWikipediaLink("wikiLink");

        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        lobby.setAnswers(answers);


        given(playerService.createPlayer(Mockito.any())).willReturn(player);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(null);
        given(answerService.createAnswer(Mockito.any())).willReturn(answer);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/answersGroupedByLetter").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());

    }

    /*
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e));
        }
    }
}
