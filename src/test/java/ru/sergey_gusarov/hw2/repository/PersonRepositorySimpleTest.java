package ru.sergey_gusarov.hw2.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.sergey_gusarov.hw2.config.AppConfigRus;
import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.exception.BizLogicException;
import ru.sergey_gusarov.hw2.exception.DaoException;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositorySimpleTest {
    private final static String PERSON_NAME = "Name1";
    private final static String PERSON_SURNAME = "Surname1";

    @Test
    @DisplayName("Поиск пользователя по фамилии и имени")
    void findByNameAndSurname() {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(AppConfigRus.class);
        context.refresh();

        PersonRepository personRepositorySimple = context.getBean(PersonRepository.class);

        Throwable exceptionNameAndSurname = assertThrows(DaoException.class, () ->
                personRepositorySimple.findByNameAndSurname("", "")
        );
        assertEquals("Пустое значение имени и фамилии пользователя", exceptionNameAndSurname.getMessage());

        Throwable exceptionName = assertThrows(DaoException.class, () ->
                personRepositorySimple.findByNameAndSurname("", PERSON_SURNAME)
        );
        assertEquals("Пустое значение имени пользователя", exceptionName.getMessage());

        Throwable exceptionSurname = assertThrows(DaoException.class, () ->
                personRepositorySimple.findByNameAndSurname(PERSON_NAME, "")
        );
        assertEquals("Пустое значение фамилии пользователя", exceptionSurname.getMessage());

        Person firstPerson = null;
        try {
            firstPerson = personRepositorySimple.findByNameAndSurname(PERSON_NAME, PERSON_SURNAME);
        } catch (DaoException e) {
            e.printStackTrace();
            assertFalse(true, "Не удалось создать firstPerson");
        }
        assertEquals(PERSON_NAME, firstPerson.getName());
        assertEquals(PERSON_SURNAME, firstPerson.getSurname());
        Person newTryFirstPerson = null;
        try {
            newTryFirstPerson = personRepositorySimple.findByNameAndSurname(PERSON_NAME, PERSON_SURNAME);
        } catch (DaoException e) {
            e.printStackTrace();
            assertFalse(true, "Не удалось создать newTryFirstPerson");
        }
        assertEquals(PERSON_NAME, newTryFirstPerson.getName());
        assertEquals(PERSON_SURNAME, newTryFirstPerson.getSurname());
        assertEquals(newTryFirstPerson, firstPerson);
        Person secondPerson = null;
        try {
            secondPerson = personRepositorySimple.findByNameAndSurname("Name2", "Surname2");
        } catch (DaoException e) {
            e.printStackTrace();
            assertFalse(true, "Не удалось создать secondPerson");
        }
        assertEquals("Name2", secondPerson.getName());
        assertEquals("Surname2", secondPerson.getSurname());
        assertFalse(secondPerson.equals(firstPerson));
    }


}