package ch.uzh.ifi.hase.soprafs22.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerGetWithTokenDTO {
    private Long id;
    private String name;
    private String playerToken;
}
