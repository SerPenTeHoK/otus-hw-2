package ru.sergey_gusarov.hw2.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.sergey_gusarov.hw2.repository.PersonRepository;
import ru.sergey_gusarov.hw2.repository.PersonRepositorySimple;
import ru.sergey_gusarov.hw2.service.testing.TestingService;
import ru.sergey_gusarov.hw2.service.testing.TestingServiceImplFile;
import ru.sergey_gusarov.hw2.service.user.PersonService;
import ru.sergey_gusarov.hw2.service.user.PersonServiceImpl;

@ComponentScan(basePackages = {
        "ru.sergey_gusarov.hw2.repository",
        "ru.sergey_gusarov.hw2.service",
        "ru.sergey_gusarov.hw2.util"}
)
@PropertySource("class path:application.properties")
//@PropertySource("#{systemProperties['user.language']}") - не признаёт SpEL
@Configuration
public class  AppConfigRus {
    @Bean
    public PropertySourcesPlaceholderConfigurer placeholderConfigurerInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasenames("/i18/ExceptionMessages", "/i18/ShellMessage");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }

    @Bean
    TestingService TestingService() {
        return new TestingServiceImplFile();
    }

    @Bean
    PersonRepository personRepository() {
        return new PersonRepositorySimple();
    }

    @Bean
    PersonService personService(PersonRepository personRepository) {
        return new PersonServiceImpl(personRepository);
    }


}
