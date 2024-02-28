package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private LobbyService lobbyService;

    private Lobby testLobby;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setHost("TestHost");
        testLobby.setRounds(1);
        testLobby.setHostToken("1234");
        testLobby.setCurrentLetter('a');
        testLobby.setRoundDuration(1);
        testLobby.setRoundStarted(false);
        testLobby.setCategoryCitiesActive(true);
        testLobby.setCategoryCountriesActive(true);
        testLobby.setCategoryMunicipalityActive(false);
        testLobby.setCategoryRiversActive(false);
        List<Character> list = new ArrayList<>();
        list.add('a');
        list.add('b');
        testLobby.setUsedLetters(list);
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testlobby
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
    }

    @Test
    void createLobby_validInputs_success() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

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
    void pollLetterOfLobby_validInputs_success() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        createdLobby.setRoundStarted(true);
        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        assertTrue(createdLobby.getRoundStarted());
        assertEquals(lobbyService.pollLetterOfLobby(createdLobby), testLobby.getCurrentLetter());
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
    void getLobbyByID_validInputs_success() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        testLobby.setId(1L);
        Lobby createdLobby = lobbyService.createLobby(lobbyService.getLobbyById(testLobby.getId()));
        lobbyService.saveLobby(createdLobby);
        // then
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));

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
    void startRound_validInputs_success() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        testLobby.setId(1L);

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        lobbyService.startRound(1);
        // then

        assertTrue(testLobby.getRoundStarted());
        assertEquals(testLobby.getCurrentLetter(), createdLobby.getCurrentLetter());
        assertEquals(testLobby.getId(), createdLobby.getId());
        assertEquals(testLobby.getHost(), createdLobby.getHost());
        assertEquals(testLobby.getRounds(), createdLobby.getRounds());
        assertEquals(testLobby.getRoundDuration(), createdLobby.getRoundDuration());
        assertEquals(testLobby.getCategoryCitiesActive(), createdLobby.getCategoryCitiesActive());
        assertEquals(testLobby.getCategoryRiversActive(), createdLobby.getCategoryRiversActive());
        assertEquals(testLobby.getCategoryCountriesActive(), createdLobby.getCategoryCountriesActive());
        assertEquals(testLobby.getCategoryMunicipalityActive(), createdLobby.getCategoryMunicipalityActive());
        assertEquals(testLobby.getRoundStarted(), createdLobby.getRoundStarted());
        assertNotNull(createdLobby.getHostToken());
    }
    @Test
    void startRound_invalidInput_NoLbbyFound_ThrowsException() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        testLobby.setId(1L);

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        createdLobby.setId(2L);
        lobbyService.startRound(1);
        // then

        assertThrows(ResponseStatusException.class, () -> lobbyService.startRound(3));
    }    @Test
    void startRound_invalidInput_RoundAlreadyStarted_ThrowsException() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        testLobby.setId(1L);

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        createdLobby.setId(2L);
        testLobby.setRoundStarted(true);
        // then

        assertThrows(ResponseStatusException.class, () -> lobbyService.startRound(1));
    }
    @Test
    void getCurrentLetterOfLobbyByLobbyId_validInputs_success() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Lobby createdLobby = lobbyService.createLobby(lobbyService.getLobbyById(testLobby.getId()));
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));

        // then
        assertEquals(testLobby.getCurrentLetter(), lobbyService.getCurrentLetterOfLobbyByLobbyId(createdLobby.getId().intValue()));
        assertEquals(testLobby.getCurrentLetter(), createdLobby.getCurrentLetter());
        assertEquals(testLobby.getId(), createdLobby.getId());
        assertEquals(testLobby.getHost(), createdLobby.getHost());
        assertEquals(testLobby.getRounds(), createdLobby.getRounds());
        assertEquals(testLobby.getRoundDuration(), createdLobby.getRoundDuration());
        assertEquals(testLobby.getCategoryCitiesActive(), createdLobby.getCategoryCitiesActive());
        assertEquals(testLobby.getCategoryRiversActive(), createdLobby.getCategoryRiversActive());
        assertEquals(testLobby.getCategoryCountriesActive(), createdLobby.getCategoryCountriesActive());
        assertEquals(testLobby.getCategoryMunicipalityActive(), createdLobby.getCategoryMunicipalityActive());
        assertEquals(testLobby.getRoundStarted(), createdLobby.getRoundStarted());
        assertNotNull(createdLobby.getHostToken());
    }
    @Test
    void getCurrentLetterOfLobbyByLobbyId_invalidInputs_NoLobbyFound_ThrowsException() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));

        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.getCurrentLetterOfLobbyByLobbyId(3));

    }
    @Test
    void getCurrentLetterOfLobbyByLobbyId_invalidInputs_RoundNotStartedYet_ThrowsException() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        testLobby.setCurrentLetter(null);

        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.getCurrentLetterOfLobbyByLobbyId(1));

    }
    @Test
    void startLobbyRound_validInputs_success() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Lobby createdLobby = lobbyService.createLobby(lobbyService.getLobbyById(testLobby.getId()));
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        lobbyService.startLobbyRound(1);
        // then
        assertTrue(testLobby.getRoundStarted());
        assertEquals(testLobby.getCurrentLetter(), createdLobby.getCurrentLetter());
        assertEquals(testLobby.getId(), createdLobby.getId());
        assertEquals(testLobby.getHost(), createdLobby.getHost());
        assertEquals(testLobby.getRounds(), createdLobby.getRounds());
        assertEquals(testLobby.getRoundDuration(), createdLobby.getRoundDuration());
        assertEquals(testLobby.getCategoryCitiesActive(), createdLobby.getCategoryCitiesActive());
        assertEquals(testLobby.getCategoryRiversActive(), createdLobby.getCategoryRiversActive());
        assertEquals(testLobby.getCategoryCountriesActive(), createdLobby.getCategoryCountriesActive());
        assertEquals(testLobby.getCategoryMunicipalityActive(), createdLobby.getCategoryMunicipalityActive());
        assertEquals(testLobby.getRoundStarted(), createdLobby.getRoundStarted());
        assertNotNull(createdLobby.getHostToken());
    }    @Test
    void startLobbyRound_invalidInputs_NoLobbyFound_ThrowsException() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        lobbyService.startLobbyRound(1);
        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.startLobbyRound(3));
    }
    @Test
    void startLobbyRound_invalidInputs_RoundAlreadyStarted_ThrowsException() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        lobbyService.startLobbyRound(1);
        testLobby.setRoundStarted(true);
        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.startLobbyRound(1));
    }
    @Test
    void getNextRoundByLobbyID_validInputs_success() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        lobbyService.getNextRoundByLobbyID(1);
        lobbyService.getLobbies();
        // then

        assertEquals(testLobby.getCurrentLetter(), createdLobby.getCurrentLetter());
        assertEquals(testLobby.getId(), createdLobby.getId());
        assertEquals(testLobby.getHost(), createdLobby.getHost());
        assertEquals(testLobby.getRounds(), createdLobby.getRounds());
        assertEquals(testLobby.getRoundDuration(), createdLobby.getRoundDuration());
        assertEquals(testLobby.getCategoryCitiesActive(), createdLobby.getCategoryCitiesActive());
        assertEquals(testLobby.getCategoryRiversActive(), createdLobby.getCategoryRiversActive());
        assertEquals(testLobby.getCategoryCountriesActive(), createdLobby.getCategoryCountriesActive());
        assertEquals(testLobby.getCategoryMunicipalityActive(), createdLobby.getCategoryMunicipalityActive());
        assertEquals(testLobby.getRoundStarted(), createdLobby.getRoundStarted());
        assertNotNull(createdLobby.getHostToken());
    }
    @Test
    void getNextRoundByLobbyID_invalidInputs_NoLobbyFound_ThrowsException() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));

        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.getNextRoundByLobbyID(2));

    }
    @Test
    void getNextRoundByLobbyID_invalidInputs_RoundNotStartedYet_ThrowsException() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        testLobby.setId(2L);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        createdLobby.setId(1L);
        createdLobby.setCurrentLetter(null);
        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.getNextRoundByLobbyID(1));

    }    @Test
    void getNextRoundByLobbyID_invalidInputs_UsedLettersUp_ThrowsException() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);

        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        // create a list of used letters to test if it throws an error if the amount is higher than 26
        List<Character> testUsedLetters = new ArrayList<>();
        testUsedLetters.add('a');testUsedLetters.add('b');testUsedLetters.add('c');testUsedLetters.add('d');
        testUsedLetters.add('e');testUsedLetters.add('f');testUsedLetters.add('g');testUsedLetters.add('h');
        testUsedLetters.add('i');testUsedLetters.add('j');testUsedLetters.add('k');testUsedLetters.add('l');
        testUsedLetters.add('m');testUsedLetters.add('n');testUsedLetters.add('o');testUsedLetters.add('p');
        testUsedLetters.add('q');testUsedLetters.add('r');testUsedLetters.add('s');testUsedLetters.add('t');
        testUsedLetters.add('u');testUsedLetters.add('v');testUsedLetters.add('w');testUsedLetters.add('x');
        testUsedLetters.add('y');testUsedLetters.add('z');
        testLobby.setUsedLetters(testUsedLetters);
        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.getNextRoundByLobbyID(1));

    }
    @Test
    void getNextLetterLobbyId_validInputs_success() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        testLobby.setRoundStarted(null);
        lobbyService.getNextLetterLobbyId(1);
        // then
        assertFalse(testLobby.getRoundStarted());
        assertEquals(testLobby.getCurrentLetter(), createdLobby.getCurrentLetter());
        assertEquals(testLobby.getId(), createdLobby.getId());
        assertEquals(testLobby.getHost(), createdLobby.getHost());
        assertEquals(testLobby.getRounds(), createdLobby.getRounds());
        assertEquals(testLobby.getRoundDuration(), createdLobby.getRoundDuration());
        assertEquals(testLobby.getCategoryCitiesActive(), createdLobby.getCategoryCitiesActive());
        assertEquals(testLobby.getCategoryRiversActive(), createdLobby.getCategoryRiversActive());
        assertEquals(testLobby.getCategoryCountriesActive(), createdLobby.getCategoryCountriesActive());
        assertEquals(testLobby.getCategoryMunicipalityActive(), createdLobby.getCategoryMunicipalityActive());
        assertEquals(testLobby.getRoundStarted(), createdLobby.getRoundStarted());
        assertNotNull(createdLobby.getHostToken());
    } @Test
    void getNextLetterLobbyId_invalidInputs_NoLobbyFound_ThrowsException() {
        // when -> any object is being save in the lobbyRepository -> return the dummy
        // testLobby
        lobbyService.createLobby(testLobby);
        Mockito.when(lobbyRepository.findById(1L)).thenReturn((testLobby));
        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.getNextLetterLobbyId(2));

    }
}