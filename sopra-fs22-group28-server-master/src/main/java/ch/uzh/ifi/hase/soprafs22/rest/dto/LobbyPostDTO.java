package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LobbyPostDTO {
    private String host;
    private Integer rounds;
    private Integer roundDuration;
    private Boolean categoryCitiesActive;
    private Boolean categoryCountriesActive;
    private Boolean categoryRiversActive;
    private Boolean categoryMunicipalityActive;
    private String customCategory1;
    private String customCategory2;
    private String customCategory3;
    private Set<Player> players;
}
