package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.Answer;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "host", target = "host")
    @Mapping(source = "rounds", target = "rounds")
    @Mapping(source = "roundDuration", target = "roundDuration")
    @Mapping(source = "categoryCitiesActive", target = "categoryCitiesActive")
    @Mapping(source = "categoryCountriesActive", target = "categoryCountriesActive")
    @Mapping(source = "categoryRiversActive", target = "categoryRiversActive")
    @Mapping(source = "categoryMunicipalityActive", target = "categoryMunicipalityActive")
    @Mapping(source = "customCategory1", target = "customCategory1")
    @Mapping(source = "customCategory2", target = "customCategory2")
    @Mapping(source = "customCategory3", target = "customCategory3")
    @Mapping(source = "players", target = "players")
    Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

    @Mapping(source = "host", target = "host")
    @Mapping(source = "rounds", target = "rounds")
    @Mapping(source = "roundDuration", target = "roundDuration")
    @Mapping(source = "categoryCitiesActive", target = "categoryCitiesActive")
    @Mapping(source = "categoryCountriesActive", target = "categoryCountriesActive")
    @Mapping(source = "categoryRiversActive", target = "categoryRiversActive")
    @Mapping(source = "categoryMunicipalityActive", target = "categoryMunicipalityActive")
    @Mapping(source = "customCategory1", target = "customCategory1")
    @Mapping(source = "customCategory2", target = "customCategory2")
    @Mapping(source = "customCategory3", target = "customCategory3")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "currentLetter", target = "currentLetter")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "answers", target = "answers")
    LobbyAnswersCurrentLetterGetDTO convertEntityToLobbyAnswersCurrentLetterGetDTO(Lobby lobby);

    @Mapping(source = "host", target = "host")
    @Mapping(source = "hostToken", target = "hostToken")
    @Mapping(source = "rounds", target = "rounds")
    @Mapping(source = "roundDuration", target = "roundDuration")
    @Mapping(source = "categoryCitiesActive", target = "categoryCitiesActive")
    @Mapping(source = "categoryCountriesActive", target = "categoryCountriesActive")
    @Mapping(source = "categoryRiversActive", target = "categoryRiversActive")
    @Mapping(source = "categoryMunicipalityActive", target = "categoryMunicipalityActive")
    @Mapping(source = "customCategory1", target = "customCategory1")
    @Mapping(source = "customCategory2", target = "customCategory2")
    @Mapping(source = "customCategory3", target = "customCategory3")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "currentLetter", target = "currentLetter")
    LobbyHostGetDTO convertEntityToLobbyHostGetDTO(Lobby lobby);


    @Mapping(source = "roundStarted", target = "roundStarted")
    @Mapping(source = "hostToken", target = "hostToken")
    Lobby convertLobbyStartDTOtoEntity(LobbyStartDTO lobbyStartDTO);

    @Mapping(source = "rounds", target = "rounds")
    @Mapping(source = "roundDuration", target = "roundDuration")
    @Mapping(source = "hostToken", target = "hostToken")
    @Mapping(source = "categoryCitiesActive", target = "categoryCitiesActive")
    @Mapping(source = "categoryCountriesActive", target = "categoryCountriesActive")
    @Mapping(source = "categoryRiversActive", target = "categoryRiversActive")
    @Mapping(source = "categoryMunicipalityActive", target = "categoryMunicipalityActive")
    Lobby convertLobbyPatchDTOtoEntity(LobbyPatchDTO lobbyPatchDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "lobbyId", target = "lobbyId")
    Player convertPlayerPostDTOtoEntity(PlayerPostDTO playerPostDTO);

    @Mapping(source = "name", target = "name")
    PlayerGetDTO convertEntityToPlayerGetDTO(Player player);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "playerToken", target = "playerToken")
    PlayerGetWithTokenDTO convertEntityToPlayerGetWithTokenDTO(Player player);

    @Mapping(source = "playerID", target = "playerID")
    @Mapping(source = "letter", target = "letter")
    @Mapping(source = "answer", target = "answer")
    @Mapping(source = "valid", target = "valid")
    @Mapping(source = "validVotes", target = "validVotes")
    @Mapping(source = "geoNameID", target = "geoNameID")
    @Mapping(source = "wikipediaLink", target = "wikipediaLink")
    @Mapping(source = "categoryVerbose", target = "categoryVerbose")
    AnswerGetDTO convertEntityToAnswerGetDTO(Answer answer);

    @Mapping(source = "playerToken", target = "playerToken")
    @Mapping(source = "answer", target = "answer")
    Answer convertAnswerPostDTOtoEntity(AnswerPostDTO answerPostDTO);

}
