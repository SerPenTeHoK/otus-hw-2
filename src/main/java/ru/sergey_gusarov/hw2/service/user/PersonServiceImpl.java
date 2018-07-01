package ru.sergey_gusarov.hw2.service.user;

import ru.sergey_gusarov.hw2.repository.PersonRepository;
import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.exception.BizLogicException;

public class PersonServiceImpl implements PersonService {
    private PersonRepository dao;

    public PersonServiceImpl(PersonRepository dao) {
        this.dao = dao;
    }

    public Person getByNameAndSurname(String name, String surname) throws BizLogicException {
        return dao.findByNameAndSurname(name, surname);
    }
}
