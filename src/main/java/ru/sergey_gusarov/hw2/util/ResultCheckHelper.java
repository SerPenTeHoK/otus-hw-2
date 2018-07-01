package ru.sergey_gusarov.hw2.util;

import ru.sergey_gusarov.hw2.domain.Answer;
import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.exception.BizLogicException;

import java.util.List;

public class ResultCheckHelper {
    public static Integer getSumScore(List<Question> questions) throws BizLogicException {
        if (questions == null)
            throw new BizLogicException("Переданы пустые вопросы, нет возможности посчитать сумму");
        Integer sumScore = 0;
        for (Question question : questions) {
            for (Answer answer : question.getAnswers()) {
                sumScore += answer.getScore();
            }
        }
        return sumScore;
    }

    public static boolean isTestPass(List<Question> questions) throws BizLogicException {
        if (questions == null)
            throw new BizLogicException("Переданы пустые вопросы, нет возможности определить пройден ли тест");
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
