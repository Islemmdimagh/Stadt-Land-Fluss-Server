package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Answer;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.repository.AnswerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see AnswerService
 */

@WebAppConfiguration
@SpringBootTest
class AnswerServiceIntegrationTest {

    @Qualifier("answerRepository")
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerService answerService;


    @BeforeEach
    public void setup() {
        answerRepository.deleteAll();
    }

    @AfterEach
    public void setup2() {
        answerRepository.deleteAll();
    }

    @Test
    void createAnswer_validInputs_success() {
        // given
        assertTrue(answerRepository.findAll().isEmpty());

        Answer testAnswer = new Answer();
        testAnswer.setAnswer("blabla");
        testAnswer.setLetter('a');
        testAnswer.setCategory(1);
        testAnswer.setPlayerToken("1234");
        testAnswer.setPlayerID(1L);

        // when
        Answer createdAnswer = answerService.createAnswer(testAnswer);
        // then
        assertEquals(testAnswer.getAnswer(), createdAnswer.getAnswer());
        assertEquals(testAnswer.getPlayerToken(), createdAnswer.getPlayerToken());
        assertEquals(testAnswer.getLetter(), createdAnswer.getLetter());
        assertEquals(testAnswer.getCategory(), createdAnswer.getCategory());
        assertEquals(testAnswer.getPlayerID(), createdAnswer.getPlayerID());
    }
    @Test
    void getAnswersLobbyById_validInputs_success() {
        // given
        assertTrue(answerRepository.findAll().isEmpty());

        Answer testAnswer = new Answer();
        testAnswer.setAnswer("blabla");
        testAnswer.setLetter('a');
        testAnswer.setCategory(1);
        testAnswer.setPlayerToken("1234");
        testAnswer.setPlayerID(1L);

        answerService.createAnswer(testAnswer);
        answerService.saveAnswer(testAnswer);
        // when
        Answer createdAnswer = answerService.getAnswerById(testAnswer.getId());
        // then
        assertEquals(testAnswer.getId(), createdAnswer.getId());
        assertEquals(testAnswer.getAnswer(), createdAnswer.getAnswer());
        assertEquals(testAnswer.getPlayerToken(), createdAnswer.getPlayerToken());
        assertEquals(testAnswer.getLetter(), createdAnswer.getLetter());
        assertEquals(testAnswer.getCategory(), createdAnswer.getCategory());
        assertEquals(testAnswer.getPlayerID(), createdAnswer.getPlayerID());
    }
}
