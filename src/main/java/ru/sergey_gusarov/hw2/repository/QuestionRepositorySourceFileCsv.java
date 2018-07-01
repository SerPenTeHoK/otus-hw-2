package ru.sergey_gusarov.hw2.repository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import ru.sergey_gusarov.hw2.domain.Answer;
import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.exception.BizLogicException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
public class QuestionRepositorySourceFileCsv implements QuestionRepository {
    private final static int QUESTION_START_NUM = 1;

    @Autowired
    private MessageSource messageSource;

    @Value("${testing.question.file}")
    private String questionsFileName;
    @Value("${testing.question.max_count}")
    private int countQuestionInFile;

    @Override
    public List<Question> findAll() throws IOException, BizLogicException {
        if (questionsFileName != null)
            return loadFile(questionsFileName);
        else
            throw new BizLogicException(messageSource.getMessage("question.file.dont.set", null,
                    Locale.getDefault()));
    }

    private List<Question> loadFile(String fileName) throws IOException, BizLogicException {
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
            throw new BizLogicException(messageSource.getMessage("question.file.error.read", null,
                    Locale.getDefault()), ex);
        }
        return questionList;
    }
}
