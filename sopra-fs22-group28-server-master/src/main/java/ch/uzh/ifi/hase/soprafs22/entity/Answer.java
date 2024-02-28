package ch.uzh.ifi.hase.soprafs22.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long playerID;

    @Column(nullable = false)
    private String playerToken;

    @Column(nullable = false)
    private Character letter;

    @Column(nullable = false)
    private String answer;

    @Column
    private Integer category;

    @Column
    private Boolean valid;

    @Column
    private Integer validVotes = 0;

    @ElementCollection
    private List<String> playersThatAlreadyVoted;


    @Column
    private String wikipediaLink;

    @Column
    private String geoNameID;

    @Column
    private Integer points;

    @Transient
    private String categoryVerbose;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "lobby")
    private Lobby lobby;

    public void validateVotes(Integer players) {
        if (validVotes > Math.ceil(players / 2f)) {
            valid = true;
        }
    }

    public void addVote(String voteSubmitterToken) {
        playersThatAlreadyVoted.add(voteSubmitterToken);
        validVotes++;
    }

    public void populateVerboseCategory() {
        switch (category) {
            case 1 -> categoryVerbose = "Cities";
            case 2 -> categoryVerbose = "Countries";
            case 3 -> categoryVerbose = "Rivers";
            case 4 -> categoryVerbose = "Municipalities";
            case 5 -> categoryVerbose = lobby.getCustomCategory1();
            case 6 -> categoryVerbose = lobby.getCustomCategory2();
            case 7 -> categoryVerbose = lobby.getCustomCategory3();
            default -> categoryVerbose = "unknown";
        }
    }
}
