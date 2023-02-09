package ru.djets.pdd_checker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.djets.pdd_checker.services.utils.DataTemplateLoader;

@SpringBootApplication
public class PddCheckerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PddCheckerApplication.class, args);
        DataTemplateLoader dataTemplateLoader = context.getBean(DataTemplateLoader.class);
        dataTemplateLoader.createQuestion(20);
    }

}
