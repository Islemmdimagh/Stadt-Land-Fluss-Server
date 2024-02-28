package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.entity.Answer;
import ch.uzh.ifi.hase.soprafs22.repository.AnswerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnswerService {
    private final Logger log = LoggerFactory.getLogger(AnswerService.class);

    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(@Qualifier("answerRepository") AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<Answer> getAnswers() {
        return this.answerRepository.findAll();
    }

    public Answer getAnswerById(Long id) {
        Optional<Answer> optionalAnswer = answerRepository.findById(id);
        Answer answer = null;
        if (optionalAnswer.isPresent()) {
            answer = optionalAnswer.get();
        }
        return answer;
    }

    public Answer createAnswer(Answer newAnswer) {
        newAnswer = answerRepository.save(newAnswer);
        answerRepository.flush();
        log.debug("Created Information for Answer: {}", newAnswer);
        return newAnswer;
    }

    public Answer saveAnswer(Answer toSaveAnswer) {
        Answer newAnswer = answerRepository.save(toSaveAnswer);
        answerRepository.flush();
        log.debug("Updated Information for Answer: {}", newAnswer);
        return newAnswer;
    }


}
