package ru.sergey_gusarov.hw2.repository;

import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.exception.BizLogicException;

public interface PersonRepository {
    Person findByNameAndSurname(String name, String surname) throws BizLogicException;
}
