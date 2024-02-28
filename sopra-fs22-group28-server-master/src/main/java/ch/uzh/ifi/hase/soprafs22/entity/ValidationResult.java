package ch.uzh.ifi.hase.soprafs22.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationResult {
    private String wikipediaLink;
    private Integer geoNameID;
    private Boolean valid;
}
