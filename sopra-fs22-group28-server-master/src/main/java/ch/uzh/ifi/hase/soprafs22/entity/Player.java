package ch.uzh.ifi.hase.soprafs22.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Player {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer lobbyId;

    @Column(updatable = false, nullable = false)
    private String playerToken = UUID.randomUUID().toString();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "lobby")
    private Lobby lobby;

    @Column
    private Integer points;

    @Transient
    // Marking as Transient as we do not want the current Answers stored in the DB. They are stored already in the Lobby.
    private List<Answer> currentAnswers;

    @Transient
    private List<Answer> finalAnswers;

}