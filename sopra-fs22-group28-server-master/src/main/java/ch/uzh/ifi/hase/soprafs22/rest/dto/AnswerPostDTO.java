package ch.uzh.ifi.hase.soprafs22.rest.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerPostDTO {
    private String playerToken;
    private Integer category;
    private String answer;
}
