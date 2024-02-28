package ch.uzh.ifi.hase.soprafs22.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LobbyPatchDTO {
    private String hostToken;
    private Integer rounds;
    private Integer roundDuration;
    private Boolean categoryCitiesActive;
    private Boolean categoryCountriesActive;
    private Boolean categoryRiversActive;
    private Boolean categoryMunicipalityActive;
}
