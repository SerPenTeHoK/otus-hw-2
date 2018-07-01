package ru.sergey_gusarov.hw2.service.user.login;

import org.springframework.stereotype.Service;
import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.exception.BizLogicException;
import ru.sergey_gusarov.hw2.service.user.PersonService;

import java.io.InputStream;
import java.util.Scanner;

@Service
public class LoginServiceImplShell implements LoginService {
    private final PersonService personService;
    private InputStream inputStream = System.in;

    public LoginServiceImplShell(PersonService personService) {
        this.personService = personService;
    }

    public void setInputStream(InputStream inputStream) throws BizLogicException {
        if (inputStream == null)
            throw new BizLogicException("Передан пустой поток!");
        this.inputStream = inputStream;
    }

    @Override
    public Person getUser() throws BizLogicException {
        String name;
        String surname;

        Scanner in = new Scanner(inputStream);
        System.out.print("Введите имя: ");
        name = in.nextLine();
        System.out.print("Введите фамилию: ");
        surname = in.nextLine();

        return personService.getByNameAndSurname(name, surname);
    }
}
