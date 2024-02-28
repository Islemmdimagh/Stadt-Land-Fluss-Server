package ch.uzh.ifi.hase.soprafs22.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerGetDTO {
    private Long id;
    private Long playerID;
    private Character letter;
    private String answer;
    private Boolean valid;
    private Integer validVotes;
    private String categoryVerbose;
    private String geoNameID;
    private String wikipediaLink;
    private Integer points;

}
