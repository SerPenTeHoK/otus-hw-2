package ru.sergey_gusarov.hw2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.sergey_gusarov.hw2.repository.QuestionRepository;
import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.domain.results.IntervieweeResultBase;
import ru.sergey_gusarov.hw2.exception.BizLogicException;
import ru.sergey_gusarov.hw2.service.testing.TestingService;
import ru.sergey_gusarov.hw2.service.testing.results.ShowResutlsService;
import ru.sergey_gusarov.hw2.service.user.login.LoginService;

import java.io.IOException;
import java.util.List;


public class UserTesting {
    private static Logger log = LoggerFactory.getLogger(UserTesting.class);

    public static void main(String[] args) {
        log.debug("Try load spring contex");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        log.debug("finish load spring contex");

        log.debug("Try get beans");
        QuestionRepository questionRepository = context.getBean(QuestionRepository.class);
        LoginService loginService = context.getBean(LoginService.class);
        TestingService testingService = context.getBean(TestingService.class);
        ShowResutlsService showResutlsService = context.getBean(ShowResutlsService.class);
        log.debug("End getting beans");

        try {
            Person interviewee = loginService.getUser();
            List<Question> questions = questionRepository.findAll();
            IntervieweeResultBase intervieweeResult = testingService.startTest(questions, interviewee);
            showResutlsService.showTestingResult(intervieweeResult);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("\nТестирование перервано обратитесь к разработчику, скопировав весь текст ошибки.");
            log.error("Real error", ex);
            return;
        } catch (BizLogicException ex) {
            ex.printStackTrace();
            ex.printMessage();
            log.error("Logic error", ex);
            return;
        }
        System.out.println("\nТестирование окончено.");
    }
}
