package ch.uzh.ifi.hase.soprafs22.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LobbyStartDTO {
    private String hostToken;
    private Boolean roundStarted;

}
