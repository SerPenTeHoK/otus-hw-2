package ru.sergey_gusarov.hw2.repository;

import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.exception.BizLogicException;
import ru.sergey_gusarov.hw2.exception.DaoException;

import java.io.IOException;
import java.util.List;

public interface QuestionRepository {
    List<Question> findAll()
            throws IOException, DaoException;
}
