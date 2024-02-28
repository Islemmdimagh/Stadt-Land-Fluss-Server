package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.ErrorMSG;
import ch.uzh.ifi.hase.soprafs22.entity.Answer;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import ch.uzh.ifi.hase.soprafs22.entity.ValidationResult;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.AnswerService;
import ch.uzh.ifi.hase.soprafs22.service.LobbyService;
import ch.uzh.ifi.hase.soprafs22.service.PlayerService;
import ch.uzh.ifi.hase.soprafs22.utils.Validator;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class LobbyController {

    private final LobbyService lobbyService;
    private final PlayerService playerService;
    private final AnswerService answerService;

    LobbyController(LobbyService lobbyService, PlayerService playerService, AnswerService answerService) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
        this.answerService = answerService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyHostGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        try {
            Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);
            Lobby createdLobby = lobbyService.createLobby(lobbyInput);
            Player playerHost = new Player();
            LobbyHostGetDTO createdLobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyHostGetDTO(createdLobby);
            playerHost.setLobbyId(Math.toIntExact(createdLobbyGetDTO.getId()));
            playerHost.setName("Host: " + createdLobby.getHost());
            playerHost.setPlayerToken(createdLobbyGetDTO.getHostToken());
            playerService.createPlayer(playerHost);
            return createdLobbyGetDTO;
        }
        catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMSG.LOBBY_CREATION_ERROR);
        }
    }

    @PostMapping("/lobbies/{lobbyID}/startRound")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String startRound(@PathVariable Long lobbyID, @RequestBody LobbyStartDTO lobbyStartDTO) {
        if (lobbyService.getLobbyById(lobbyID) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        else {
            Lobby lobby = lobbyService.getLobbyById(lobbyID);
            if (Boolean.TRUE.equals(lobby.getRoundStarted())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.ROUND_STARTED_ALREADY);
            }
            Lobby lobbyStart = DTOMapper.INSTANCE.convertLobbyStartDTOtoEntity(lobbyStartDTO);
            if (!Objects.equals(lobby.getHostToken(), lobbyStart.getHostToken())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMSG.ONLY_HOST_LOBBY_CHANGE);
            }
            lobby.setRoundStarted(lobbyStart.getRoundStarted());
            String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";
            return new JSONObject(content).toString();
        }
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobbyById(@PathVariable Long lobbyId) {
        if (lobbyService.getLobbyById(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        else {
            Lobby lobby = lobbyService.getLobbyById(lobbyId);
            return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
        }
    }

    @PostMapping("/lobbies/{lobbyId}/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PlayerGetWithTokenDTO createPlayer(@PathVariable Long lobbyId, @RequestBody PlayerPostDTO playerPostDTO) {
        Player playerInput = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        Player createdPlayer = playerService.createPlayer(playerInput);
        return DTOMapper.INSTANCE.convertEntityToPlayerGetWithTokenDTO(createdPlayer);
    }

    @GetMapping("/lobbies/{lobbyId}/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlayerGetDTO> getPlayersByLobbyId(@PathVariable Long lobbyId) {
        ArrayList<PlayerGetDTO> playersGetDTOs;
        if (playerService.getPlayersByLobbyID(lobbyId.intValue()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        else {
            Lobby lobby = lobbyService.getLobbyById(lobbyId);
            List<Player> players = lobby.getPlayers();
            playersGetDTOs = new ArrayList<>();
            for (Player player : players) {
                playersGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player));
            }
        }
        return playersGetDTOs;
    }


    @GetMapping("/lobbies/{lobbyId}/letter")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Character getCurrentLetterOfLobbyByLobbyId(@PathVariable Long lobbyId) {
        if (lobbyService.getLobbyById(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        else {
            Lobby lobby = lobbyService.getLobbyById(lobbyId);
            if (lobby.getCurrentLetter() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMSG.ROUND_NOT_STARTED_YET);
            }
            return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby).getCurrentLetter();
        }
    }

    @PatchMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO startLobbyRound(@PathVariable Long lobbyId, @RequestBody LobbyPatchDTO lobbyPatchDTO) {
        if (lobbyService.getLobbyById(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }

        else {
            Lobby lobby = lobbyService.getLobbyById(lobbyId);
            Lobby lobbyPatch = DTOMapper.INSTANCE.convertLobbyPatchDTOtoEntity(lobbyPatchDTO);
            if (!Objects.equals(lobby.getHostToken(), lobbyPatch.getHostToken())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMSG.ONLY_HOST_LOBBY_CHANGE);
            }
            if (Boolean.TRUE.equals(lobby.getRoundStarted())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMSG.CANNOT_CHANGE_SETTINGS_AFTER_GAME_START);
            }
            lobby.setRounds(lobbyPatch.getRounds());
            lobby.setRoundDuration(lobbyPatch.getRoundDuration());
            lobby.setCategoryCitiesActive(lobbyPatch.getCategoryCitiesActive());
            lobby.setCategoryCountriesActive(lobbyPatch.getCategoryCountriesActive());
            lobby.setCategoryRiversActive(lobbyPatch.getCategoryRiversActive());
            lobby.setCategoryMunicipalityActive(lobbyPatch.getCategoryMunicipalityActive());
            lobby = lobbyService.saveLobby(lobby);
            return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
        }

    }

    @PostMapping("/lobbies/{lobbyId}/nextRound")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getNextRoundLobbyId(@PathVariable Long lobbyId, @RequestBody LobbyNextRoundDTO lobbyNextRoundDTO) {
        if (lobbyService.getLobbyById(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        String hostToken = lobbyNextRoundDTO.getHostToken();
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        if (!Objects.equals(hostToken, lobby.getHostToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMSG.ONLY_HOST_LOBBY_CHANGE);
        }
        if (lobby.getCurrentLetter() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.ROUND_NOT_STARTED_YET);
        }
        if (lobby.getUsedLetters().size() > 25) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.LOBBY_ALPHABET_USED_UP);
        }
        // check if the all rounds of lobby have already been played
        // add 1 to getUsedLetters.size() because the last played letter is not added yet
        if (lobby.getUsedLetters().size() + 1 >= lobby.getRounds()) {
            // game ends
            lobby.setGameFinished(true);
            // quick workaround: set SubmissionOpen to true so players get redirected without having to create another
            // api call in the client
            lobby.setSubmissionOpen(true);
            lobbyService.saveLobby(lobby);
            String content = "{'nextLetter': null}";
            return new JSONObject(content).toString();

        }
        String content = "{'nextLetter':" + lobbyService.pollLetterOfLobby(lobby) + "}";
        lobby.setSubmissionOpen(true);
        lobbyService.saveLobby(lobby);
        return new JSONObject(content).toString();
    }

    @GetMapping("/lobbies/{lobbyId}/gameRunning")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getNextLetterLobbyId(@PathVariable Long lobbyId) {
        if (lobbyService.getLobbyById(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        Boolean roundStarted = lobbyService.getLobbyById(lobbyId).getRoundStarted();
        // we want to avoid returning null
        if (roundStarted == null) {
            roundStarted = false;
        }
        String content = "{'gameRunning':" + roundStarted + "}";
        return new JSONObject(content).toString();
    }

    @GetMapping("/lobbies/{lobbyId}/gameFinished")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getGameFinished(@PathVariable Long lobbyId) {
        if (lobbyService.getLobbyById(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        Boolean gameFinished = lobbyService.getLobbyById(lobbyId).getGameFinished();
        String content = "{'gameFinished':" + gameFinished + "}";
        return new JSONObject(content).toString();
    }

    @PostMapping("/lobbies/{lobbyID}/submitAnswer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AnswerGetDTO checkSubmitAnswer(@PathVariable Long lobbyID, @RequestBody AnswerPostDTO answerPostDTO) throws IOException, InterruptedException {
        if (lobbyService.getLobbyById(lobbyID) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        Answer postAnswer = DTOMapper.INSTANCE.convertAnswerPostDTOtoEntity(answerPostDTO);
        if (playerService.getPlayerByPlayerToken(postAnswer.getPlayerToken()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.PLAYER_TOKEN_DOES_NOT_EXIST);
        }
        if (lobbyService.getLobbyById(lobbyID).getCurrentLetter() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.ROUND_NOT_STARTED_YET);
        }
        Player player = playerService.getPlayerByPlayerToken(postAnswer.getPlayerToken());
        Lobby lobby = lobbyService.getLobbyById(Long.valueOf(player.getLobbyId()));
        postAnswer.setLetter(lobby.getCurrentLetter());
        postAnswer.setPlayerID(player.getId());

        ValidationResult result = Validator.validate(postAnswer.getCategory(), postAnswer.getAnswer(), postAnswer.getLetter());
        if (result != null) {
            postAnswer.setPoints(1);
            postAnswer.setValid(result.getValid());
            postAnswer.setGeoNameID(result.getGeoNameID().toString());
            postAnswer.setWikipediaLink(result.getWikipediaLink());
        }
        else {
            postAnswer.setPoints(0);
            postAnswer.setValid(false);
        }

        lobbyService.getLobbyById(lobbyID).addAnswer(postAnswer);
        lobby.setSubmissionOpen(false);
        return DTOMapper.INSTANCE.convertEntityToAnswerGetDTO(answerService.createAnswer(postAnswer));
    }

    @GetMapping("/lobbies/{lobbyId}/submissionOpen")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getSubmissionOpen(@PathVariable Long lobbyId) {
        //Checks if submission is still possible in current round of given lobby
        if (lobbyService.getLobbyById(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        Boolean submissionOpen = lobbyService.getLobbyById(lobbyId).getSubmissionOpen();
        // we want to avoid returning null
        if (submissionOpen == null) {
            submissionOpen = false;
        }
        String content = "{'submissionOpen':" + submissionOpen + "}";
        return new JSONObject(content).toString();
    }

    @GetMapping("/lobbies/{lobbyId}/answersOfCurrentLetter")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyAnswersCurrentLetterGetDTO getAnswersOfCurrentLetter(@PathVariable Long lobbyId) {
        if (lobbyService.getLobbyById(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobby.populateCategoryVerboseInAnswers();
        List<Answer> answers = lobby.getAnswers();
        // add all answers of current letter to filteredAnswers of lobby
        List<Answer> filteredAnswers = answers.stream().filter(
                p -> p.getLetter().equals(lobby.getCurrentLetter())
        ).collect(
                Collectors.toList()
        );
        lobby.setAnswers(filteredAnswers);
        // add all answers of current letter of each player to each player
        int i;
        for (i = 0; i < lobby.getPlayers().size(); i++) {
            Player player = lobby.getPlayers().get(i);
            player.setCurrentAnswers(
                    answers.stream().filter(
                            p -> p.getLetter().equals(lobby.getCurrentLetter())
                    ).filter(
                            p -> Objects.equals(p.getPlayerID(), player.getId())
                    ).collect(
                            Collectors.toList()
                    )
            );
            List<Answer> currentAnswers = player.getCurrentAnswers();
            currentAnswers.sort(Comparator.comparing(Answer::getCategory));
            player.setCurrentAnswers(currentAnswers);
        }
        return DTOMapper.INSTANCE.convertEntityToLobbyAnswersCurrentLetterGetDTO(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/endgame")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO endGameInfo(@PathVariable Long lobbyId) {
        if (lobbyService.getLobbyById(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobby.checkValidityByVotes();
        // calculate the points per player
        List<Answer> answers = lobby.getAnswers();
        int i;
        // get answers grouped by player
        for (i = 0; i < lobby.getPlayers().size(); i++) {
            Player player = lobby.getPlayers().get(i);
            player.setPoints(0);
            Player finalPlayer = player;
            List<Answer> playerAnswerList = answers.stream().filter(
                    p -> Objects.equals(p.getPlayerID(), finalPlayer.getId())
            ).collect(
                    Collectors.toList()
            );
            for (Answer a : playerAnswerList) {
                a.populateVerboseCategory();
                int answerPoints = Boolean.TRUE.equals(a.getValid()) ? 1 : 0;
                player.setPoints(player.getPoints() + answerPoints);
                player = playerService.savePlayer(player);
            }
        }
        Lobby finalLobby = lobbyService.getLobbyById(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(finalLobby);
    }

    @PostMapping("/answers/{answerID}/submitVote")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public AnswerGetDTO voteAnswer(@PathVariable Long answerID, @RequestBody AnswerVoteDTO answerVoteDTO) {
        if (answerService.getAnswerById(answerID) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.ANSWER_DOES_NOT_EXIST);
        }
        if (playerService.getPlayerByPlayerToken(answerVoteDTO.getPlayerToken()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.PLAYER_TOKEN_DOES_NOT_EXIST);
        }
        Answer answer = answerService.getAnswerById(answerID);
        if (answer.getPlayersThatAlreadyVoted().contains(answerVoteDTO.getPlayerToken())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMSG.PLAYER_ALREADY_VOTED);
        }
        answer.addVote(answerVoteDTO.getPlayerToken());
        answerService.saveAnswer(answer);
        return DTOMapper.INSTANCE.convertEntityToAnswerGetDTO(answer);
    }

    @GetMapping("/lobbies/{lobbyId}/answersGroupedByLetter")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<Character, List<Map<String, List<Answer>>>> answersGroupedByLetter(@PathVariable Long lobbyId) {
        // GROUP ANSWERS BY LETTER, THEN GROUP BY PLAYER
        // Letter 1 -> PLayer 1, 2; Letter 2 -> Player 1, 2 ....
        if (lobbyService.getLobbyById(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMSG.LOBBY_DOES_NOT_EXIST);
        }
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobby.populateCategoryVerboseInAnswers();
        lobby.checkValidityByVotes();
        Map<Character, List<Map<String, List<Answer>>>> answersByCharacterPlayer = new HashMap<>();
        lobby.getUsedLetters().add(lobby.getCurrentLetter());
        lobbyService.saveLobby(lobby);
        for (Character letter : lobby.getUsedLetters()) {
            List<Map<String, List<Answer>>> placeHolderList = new ArrayList<>();
            answersByCharacterPlayer.put(letter, placeHolderList);
            for (Player player : lobby.getPlayers()) {
                Map<String, List<Answer>> answersByPlayer = new HashMap<>();
                // get all answers of player, filter by letter
                List<Answer> answersOfPlayer = lobby.getAnswers().stream().filter(
                        p -> p.getPlayerID().equals(player.getId())
                ).filter(
                        a -> a.getLetter().equals(letter)
                ).collect(
                        Collectors.toList()
                );
                answersByPlayer.put(player.getName(), answersOfPlayer);
                answersByCharacterPlayer.get(letter).add(answersByPlayer);
            }

        }
        return answersByCharacterPlayer;
    }


}
