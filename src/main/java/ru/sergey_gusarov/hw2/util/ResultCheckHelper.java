package ru.sergey_gusarov.hw2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import ru.sergey_gusarov.hw2.domain.Answer;
import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.exception.BizLogicException;

import java.util.List;
import java.util.Locale;

public class ResultCheckHelper {
    @Autowired
    private static MessageSource messageSource;

    public Integer getSumScore(List<Question> questions) throws BizLogicException {
        if (questions == null)
            throw new BizLogicException(messageSource.getMessage("check.result.helper.emptyQuestion.sum", null, Locale.getDefault()));
        Integer sumScore = 0;
        for (Question question : questions) {
            for (Answer answer : question.getAnswers()) {
                sumScore += answer.getScore();
            }
        }
        return sumScore;
    }

    public boolean isTestPass(List<Question> questions) throws BizLogicException {
        if (questions == null)
            throw new BizLogicException(messageSource.getMessage("check.result.helper.emptyQuestion.pass", null, Locale.getDefault()));
        for (Question question : questions) {
            Integer answerScore = 0;
            for (Answer answer : question.getAnswers()) {
                answerScore += answer.getScore();
            }
            if (question.getCheckScore() != answerScore)
                return false;
        }
        return true;
    }

}
