package ru.sergey_gusarov.hw2.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.exception.BizLogicException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionRepositorySourceFileCsvTest {

    @Test
    @DisplayName("Получение вопросов")
    void findAll() {
        QuestionRepositorySourceFileCsv questionDaoSourceFileCsv = new QuestionRepositorySourceFileCsv();
        List<Question> questions = null;
        try {
            questions = questionDaoSourceFileCsv.findAll();
        } catch (IOException ex) {
            ex.printStackTrace();
            assertTrue(false, "IOException");
        } catch (BizLogicException ex) {
            ex.printStackTrace();
            assertTrue(false, "BizLogicException");
        }
        assertNotNull(questions, "Объект с вопросами пустой");
        assertEquals("Мне и так норм", questions.get(1).getAnswers().get(0).getAnswerText(), "Чтение из файла текста ответа");

    }

}