package ch.uzh.ifi.hase.soprafs22.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "Lobby")
public class Lobby implements Serializable {
    // Kategorien: städte/dörfer (mehr als 500 einwohner), länder, flüsse,  schweizer gemeinden
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String host;

    @Column(updatable = false, nullable = false)
    private String hostToken = UUID.randomUUID().toString();

    @Column(nullable = false)
    private Integer rounds;

    @Column
    private Boolean submissionOpen = true;
    // true if there has been no submissions yet

    @Column
    private Boolean gameFinished = false;

    @Column
    private Boolean roundStarted = false;
    // duration of a round in seconds

    @Column(nullable = false)
    private Integer roundDuration;

    @Column
    private Boolean categoryCitiesActive;

    @Column
    private Boolean categoryCountriesActive;

    @Column
    private Boolean categoryRiversActive;

    @Column
    private Boolean categoryMunicipalityActive;

    @Column
    private String customCategory1;

    @Column
    private String customCategory2;

    @Column
    private String customCategory3;

    @JsonManagedReference
    @OneToMany(mappedBy = "lobby")
    private List<Player> players; //NOSONAR

    @JsonManagedReference
    @OneToMany(mappedBy = "lobby")
    private List<Answer> answers; //NOSONAR

    @ElementCollection
    private List<Character> usedLetters;
    @Column
    private Character currentLetter;
    private Random rand = new Random(); //NOSONAR - False positive, secure cryptography is not needed here

    public void checkValidityByVotes() {
        for (Answer answer : answers) {
            answer.validateVotes(players.size());
        }
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setLobby(this);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.setLobby(null);
    }

    public List<Player> getPlayers() {
        if (players != null) {
            players.sort(Comparator.comparing(Player::getId));
        }
        return players;
    }
    public void populateCategoryVerboseInAnswers() {
        for (Answer a: answers) {
            a.populateVerboseCategory();
        }
    }

    public Character getRandomChar() {
        return (char) (rand.nextInt(26) + 'a');
    }

    public void addAnswer(Answer postAnswer) {
        answers.add(postAnswer);
        postAnswer.setLobby(this);
    }
}
