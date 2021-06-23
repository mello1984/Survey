package ru.butakov.survey.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.butakov.survey.dao.QuestionRepository;

@Configuration
@AllArgsConstructor
public class AppConfig {
    private final ApplicationContext context;

    @Bean
    public QuestionRepository questionRepository(@Value("${question.repository}") String qualifier){
        return (QuestionRepository) context.getBean(qualifier);
    }
}
