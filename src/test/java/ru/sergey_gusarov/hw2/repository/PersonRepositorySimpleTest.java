package ru.sergey_gusarov.hw2.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.exception.BizLogicException;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositorySimpleTest {
    private final static String PERSON_NAME = "Name1";
    private final static String PERSON_SURNAME = "Surname1";

    @Test
    @DisplayName("Поиск пользователя по фамилии и имени")
    void findByNameAndSurname() {
        PersonRepositorySimple personDaoSimple = new PersonRepositorySimple();

        Throwable exceptionNameAndSurname = assertThrows(BizLogicException.class, () ->
                personDaoSimple.findByNameAndSurname("", "")
        );
        assertEquals("Пустое значение имени и фамилии пользователя", exceptionNameAndSurname.getMessage());

        Throwable exceptionName = assertThrows(BizLogicException.class, () ->
                personDaoSimple.findByNameAndSurname("", PERSON_SURNAME)
        );
        assertEquals("Пустое значение имени пользователя", exceptionName.getMessage());

        Throwable exceptionSurname = assertThrows(BizLogicException.class, () ->
                personDaoSimple.findByNameAndSurname(PERSON_NAME, "")
        );
        assertEquals("Пустое значение фамилии пользователя", exceptionSurname.getMessage());

        Person firstPerson = null;
        try {
            firstPerson = personDaoSimple.findByNameAndSurname(PERSON_NAME, PERSON_SURNAME);
        } catch (BizLogicException e) {
            e.printStackTrace();
            assertFalse(true, "Не удалось создать firstPerson");
        }
        assertEquals(PERSON_NAME, firstPerson.getName());
        assertEquals(PERSON_SURNAME, firstPerson.getSurname());
        Person newTryFirstPerson = null;
        try {
            newTryFirstPerson = personDaoSimple.findByNameAndSurname(PERSON_NAME, PERSON_SURNAME);
        } catch (BizLogicException e) {
            e.printStackTrace();
            assertFalse(true, "Не удалось создать newTryFirstPerson");
        }
        assertEquals(PERSON_NAME, newTryFirstPerson.getName());
        assertEquals(PERSON_SURNAME, newTryFirstPerson.getSurname());
        assertEquals(newTryFirstPerson, firstPerson);
        Person secondPerson = null;
        try {
            secondPerson = personDaoSimple.findByNameAndSurname("Name2", "Surname2");
        } catch (BizLogicException e) {
            e.printStackTrace();
            assertFalse(true, "Не удалось создать secondPerson");
        }
        assertEquals("Name2", secondPerson.getName());
        assertEquals("Surname2", secondPerson.getSurname());
        assertFalse(secondPerson.equals(firstPerson));
    }


}