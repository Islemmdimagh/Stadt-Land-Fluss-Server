package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Answer;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LobbyAnswersCurrentLetterGetDTO {
    private Long id;
    private Set<Player> players;
    private Set<Answer> answers;
}
