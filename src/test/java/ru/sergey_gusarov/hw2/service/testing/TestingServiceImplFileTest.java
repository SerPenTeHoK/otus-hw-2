package ru.sergey_gusarov.hw2.service.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.sergey_gusarov.hw2.config.AppConfigRus;
import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.domain.results.IntervieweeResultBase;
import ru.sergey_gusarov.hw2.exception.BizLogicException;
import ru.sergey_gusarov.hw2.exception.DaoException;
import ru.sergey_gusarov.hw2.repository.QuestionRepository;
import ru.sergey_gusarov.hw2.util.ResultCheckHelper;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestingServiceImplFileTest {
    private final static int CORRECT_SCORE = 7;

    private List<Question> dummyQuestion(QuestionRepository questionRepository) {
        List<Question> questions = null;
        try {
            questions = questionRepository.findAll();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
        return questions;
    }

    @Test
    @DisplayName("Тестирование по успешному пути и тестирование пройдено")
    void startTest() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(AppConfigRus.class);
        context.refresh();

        QuestionRepository questionRepository = context.getBean(QuestionRepository.class);
        TestingService testingService = context.getBean(TestingService.class);

        Person person = new Person("Name1", "Surname1");
        List<Question> questions = dummyQuestion(questionRepository);
        assertNotNull(questions, "Объект с вопросами пустой");
        IntervieweeResultBase intervieweeResult = null;

        String text = "3\n2,3\n1\n1\n1,2";
        byte[] buffer = text.getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(buffer);
        BufferedInputStream bis = new BufferedInputStream(in);

        try {
            ((TestingServiceImplFile) testingService).setInputStream(bis);
            intervieweeResult = testingService.startTest(questions, person);
        } catch (BizLogicException e) {
            e.printStackTrace();
        }
        assertNotNull(intervieweeResult, "Результат тестирования");
        assertEquals("Name1 Surname1", intervieweeResult.getPerson().getFullName());
        try {
            assertTrue((new ResultCheckHelper()).isTestPass(intervieweeResult.getQuestions()), "Тестирование пройдено");
            assertTrue((new ResultCheckHelper()).getSumScore(intervieweeResult.getQuestions()) == CORRECT_SCORE,
                    "Набрано правильное количество баллов");
        } catch (BizLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Тестирование по успешному пути и тестирование не пройдено")
    void startTestFailResult() {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(AppConfigRus.class);
        context.refresh();

        QuestionRepository questionRepository = context.getBean(QuestionRepository.class);
        TestingService testingService = context.getBean(TestingService.class);

        Person person = new Person("Name1", "Surname1");
        List<Question> questions = dummyQuestion(questionRepository);
        assertNotNull(questions, "Объект с вопросами пустой");
        IntervieweeResultBase intervieweeResult = null;

        String text = "3\n2\n1\n1\n1,2";
        byte[] buffer = text.getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(buffer);
        BufferedInputStream bis = new BufferedInputStream(in);

        try {
            ((TestingServiceImplFile) testingService).setInputStream(bis);
            intervieweeResult = testingService.startTest(questions, person);
        } catch (BizLogicException e) {
            e.printStackTrace();
        }
        assertNotNull(intervieweeResult, "Результат тестирования");
        assertEquals("Name1 Surname1", intervieweeResult.getPerson().getFullName());
        try {
            assertFalse((new ResultCheckHelper()).isTestPass(intervieweeResult.getQuestions()), "Тестирование пройдено");
            assertTrue((new ResultCheckHelper()).getSumScore(intervieweeResult.getQuestions()) < CORRECT_SCORE,
                    "Набрано меньше правильного количество баллов");
        } catch (BizLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Тестирование c ошибкой ввода")
    void startTestErrorInput() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(AppConfigRus.class);
        context.refresh();

        QuestionRepository questionRepository = context.getBean(QuestionRepository.class);
        TestingService testingService = context.getBean(TestingService.class);

        Person person = new Person("Name1", "Surname1");
        List<Question> questions = dummyQuestion(questionRepository);
        assertNotNull(questions, "Объект с вопросами пустой");

        String text = "3\n 2a \n1\n1\n1,2";
        byte[] buffer = text.getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(buffer);
        BufferedInputStream bis = new BufferedInputStream(in);
        try {
            ((TestingServiceImplFile) testingService).setInputStream(bis);
        } catch (BizLogicException e) {
            e.printStackTrace();
        }
        Throwable exceptionTextInput = assertThrows(BizLogicException.class, () ->
                testingService.startTest(questions, person)
        );
        assertEquals("Не удалось распознать, что вы ввели: \" 2a \"",
                exceptionTextInput.getMessage(), "Ошибка ввода текста");

        String text2 = "5\n2\n1\n1\n1,2";
        byte[] buffer2 = text2.getBytes();
        ByteArrayInputStream in2 = new ByteArrayInputStream(buffer2);
        BufferedInputStream bis2 = new BufferedInputStream(in2);
        try {
            ((TestingServiceImplFile) testingService).setInputStream(bis2);
        } catch (BizLogicException e) {
            e.printStackTrace();
        }
        Throwable exceptionOutOfBound = assertThrows(BizLogicException.class, () ->
                testingService.startTest(questions, person)
        );
        assertEquals("Вы ввели значение выходящее за диапазон возможных ответов : \"5\"",
                exceptionOutOfBound.getMessage(), "Ошибка ввода номера ответа больше чем их");


    }
}