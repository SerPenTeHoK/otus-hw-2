package ru.sergey_gusarov.hw2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.sergey_gusarov.hw2.config.AppConfigDefault;
import ru.sergey_gusarov.hw2.config.AppConfigRus;
import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.domain.results.IntervieweeResultBase;
import ru.sergey_gusarov.hw2.exception.BizLogicException;
import ru.sergey_gusarov.hw2.exception.DaoException;
import ru.sergey_gusarov.hw2.repository.QuestionRepository;
import ru.sergey_gusarov.hw2.service.testing.TestingService;
import ru.sergey_gusarov.hw2.service.testing.results.ShowResutlsService;
import ru.sergey_gusarov.hw2.service.user.login.LoginService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class UserTesting {
    private static Logger log = LoggerFactory.getLogger(UserTesting.class);

    public static void main(String[] args) {
        //Locale.setDefault(Locale.ENGLISH); //- для настройки, если не выбирается автоматически
        System.out.println(Locale.getDefault());

        log.debug("Try load spring context");
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(AppConfigRus.class);

        if ("ru_RU".equals(Locale.getDefault().toString()))
            context.register(AppConfigRus.class);
        else
            context.register(AppConfigDefault.class);

        context.refresh();
        log.debug("Finish load spring context");

        log.debug("Try get beans");
        QuestionRepository questionRepository = context.getBean(QuestionRepository.class);
        LoginService loginService = context.getBean(LoginService.class);
        TestingService testingService = context.getBean(TestingService.class);
        ShowResutlsService showResutlsService = context.getBean(ShowResutlsService.class);
        log.debug("Finish getting beans");

        try {
            Person interviewee = loginService.getUser();
            List<Question> questions = questionRepository.findAll();
            IntervieweeResultBase intervieweeResult = testingService.startTest(questions, interviewee);
            showResutlsService.showTestingResult(intervieweeResult);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("\n" + context.getMessage("main.exception", null, Locale.getDefault()));
            log.error("Real error", ex);
            return;
        } catch (DaoException ex) {
            ex.printStackTrace();
            ex.printMessage();
            log.error("Dao error", ex);
            return;
        } catch (BizLogicException ex) {
            ex.printStackTrace();
            ex.printMessage();
            log.error("Logic error", ex);
            return;
        }

        System.out.println("\n" + context.getMessage("main.end.test", null, Locale.getDefault()));
    }
}
