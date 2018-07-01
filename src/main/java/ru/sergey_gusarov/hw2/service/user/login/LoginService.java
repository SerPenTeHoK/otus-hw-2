package ru.sergey_gusarov.hw2.service.user.login;

import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.exception.BizLogicException;

public interface LoginService {
    Person getUser() throws BizLogicException;
}
