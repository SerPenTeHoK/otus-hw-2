package ru.sergey_gusarov.hw2.service.testing;

import ru.sergey_gusarov.hw2.domain.Answer;
import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.domain.results.IntervieweeResultBase;
import ru.sergey_gusarov.hw2.domain.results.IntervieweeResultSimple;
import ru.sergey_gusarov.hw2.exception.BizLogicException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestingServiceImplFile implements TestingService {

    private final String ANSWER_SEPARATOR_SYMBOL = ",";
    private final String HOW_TYPE_ANSWER = "Введите номер ответа. Если несколько, то через запятую, например, 2,3 : ";

    private InputStream inputStream = System.in;

    public void setInputStream(InputStream inputStream) throws BizLogicException {
        if (inputStream == null)
            throw new BizLogicException("Передан пустоей поток!");
        this.inputStream = inputStream;
    }

    @Override
    public IntervieweeResultBase startTest(List<Question> questions, Person interviewee) throws BizLogicException {

        IntervieweeResultBase intervieweeResultBase = new IntervieweeResultSimple(interviewee);
        Integer countQuestion;
        List<Question> intervieweeQuestions = new ArrayList<Question>();
        countQuestion = questions.size();

        Scanner inScanner = new Scanner(inputStream);
        System.out.println("Начинаем тестирование. Всего вопросов: " + countQuestion.toString());
        try {
            for (int i = 0; i < countQuestion; i++) {
                Question question = questions.get(i);
                printQuestionAndAnswers(question);
                String answerStr = getShellAnswer(inScanner);
                Question intervieweeQuestion = getParseAnswerAndSetQuestion(question, answerStr);
                intervieweeQuestions.add(intervieweeQuestion);
            }
        } catch (BizLogicException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizLogicException("Серёзная ошибка при провередении тестирования.", ex);
        }
        intervieweeResultBase.setQuestions(intervieweeQuestions);
        return intervieweeResultBase;
    }


    private String getShellAnswer(Scanner scanner) {
        System.out.println(HOW_TYPE_ANSWER);
        return scanner.nextLine();
    }

    private void printQuestionAndAnswers(Question question) {
        System.out.println("Вопрос №" + question.getOrdinalNum());
        System.out.println(question.getQuestionText());
        System.out.println("Варианты ответа:");
        Integer numAnswer = 1;
        for (Answer ans : question.getAnswers()) {
            System.out.println(numAnswer.toString() + " " + ans.getAnswerText());
            numAnswer++;
        }
    }

    private Question getParseAnswerAndSetQuestion(Question question, String answerStr) throws BizLogicException {
        List<Answer> resultAnswers = new ArrayList<Answer>();
        try {
            if (answerStr.contains(ANSWER_SEPARATOR_SYMBOL)) {
                String multiAnswer[] = answerStr.split(ANSWER_SEPARATOR_SYMBOL);
                for (String strToPacer : multiAnswer)
                    setIntervieweeAnswers(question, resultAnswers, strToPacer);
            } else {
                setIntervieweeAnswers(question, resultAnswers, answerStr);
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            throw new BizLogicException("Не удалось распознать, что вы ввели: \"" + answerStr + "\"");
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
            throw new BizLogicException("Вы ввели значение выходящее за диапазон возможных ответов : \"" + answerStr.trim() + "\"");
        }
        return new Question(question.getId(), question.getCheckScore(), resultAnswers);
    }

    private void setIntervieweeAnswers(Question question, List<Answer> resultAnswers, String strToPacer) throws IndexOutOfBoundsException, NumberFormatException {
        strToPacer = strToPacer.trim();
        Integer answerNum = Integer.valueOf(strToPacer) - 1;
        Answer resultAnswer = new Answer(question.getAnswers().get(answerNum).getAnswerText(),
                question.getAnswers().get(answerNum).getScore());
        resultAnswers.add(resultAnswer);
    }

}
