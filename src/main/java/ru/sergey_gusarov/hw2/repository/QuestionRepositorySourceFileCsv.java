package ru.sergey_gusarov.hw2.repository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import ru.sergey_gusarov.hw2.domain.Answer;
import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.exception.BizLogicException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QuestionRepositorySourceFileCsv implements QuestionRepository {
    private final static int QUESTION_START_NUM = 1;

    @Override
    public List<Question> findAll() throws IOException, BizLogicException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/app.property"));
        String questionsFileName = properties.getProperty("testing.question.file",
                "src/main/resources/testQuestions.csv");
        if (questionsFileName != null)
            return loadFile(questionsFileName);
        else
            throw new BizLogicException("Не указан файл из которого необходимо прочитать вопросы");

    }

    private List<Question> loadFile(String fileName) throws IOException, BizLogicException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/app.property"));
        Integer countQuestionInFile = Integer.valueOf(properties.getProperty("testing.question.max_count",
                "4"));

        List<Question> questionList = new ArrayList<Question>();
        Reader inCsvFile;
        try {
            inCsvFile = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(inCsvFile);
            Integer countQuestions = QUESTION_START_NUM;
            for (CSVRecord record : records) {
                List<Answer> answers = new ArrayList<Answer>();
                for (Integer i = QUESTION_START_NUM; i <= countQuestionInFile; i++) {
                    String colName = "Answer" + i.toString();
                    String answerStr = record.get(colName);
                    Integer score = Integer.valueOf(record.get("Score" + i.toString()));
                    answers.add(new Answer(answerStr, score));
                }
                Integer idQuestion = Integer.valueOf(record.get("Id"));
                String questionStr = record.get("Question");
                Integer checkScore = Integer.valueOf(record.get("CheckScore"));
                Question question = new Question(idQuestion, countQuestions++, questionStr, checkScore, answers);
                questionList.add(question);
            }
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } catch (IllegalStateException | IllegalArgumentException ex) {
            throw new BizLogicException("При чтение содержимого файла c вопросами произошла ошибка, ошибка в данных или настройках чтения", ex);
        }
        return questionList;
    }
}
