package ru.sergey_gusarov.hw2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.sergey_gusarov.hw2.domain.Person;
import ru.sergey_gusarov.hw2.domain.Question;
import ru.sergey_gusarov.hw2.domain.results.IntervieweeResultBase;
import ru.sergey_gusarov.hw2.exception.BizLogicException;
import ru.sergey_gusarov.hw2.repository.QuestionRepository;
import ru.sergey_gusarov.hw2.service.testing.TestingService;
import ru.sergey_gusarov.hw2.service.testing.results.ShowResutlsService;
import ru.sergey_gusarov.hw2.service.user.login.LoginService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class UserTesting {
    private static Logger log = LoggerFactory.getLogger(UserTesting.class);

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurerInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args) {
        log.debug("Try load spring contex");
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(UserTesting.class);
        log.debug("Finish load spring context");

        Locale.setDefault(Locale.ENGLISH);
        Locale l = Locale.getDefault();
        System.out.println(l.toLanguageTag());

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
        } catch (BizLogicException ex) {
            ex.printStackTrace();
            ex.printMessage();
            log.error("Logic error", ex);
            return;
        }
        System.out.println("\n"+context.getMessage("main.end.test", null, Locale.getDefault()));
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasenames("/i18/ExceptionMessages", "/i18/ShellMessage");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }

}
