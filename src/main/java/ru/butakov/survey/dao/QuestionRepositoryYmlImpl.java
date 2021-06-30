package ru.butakov.survey.dao;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.butakov.survey.aop.Loggable;
import ru.butakov.survey.config.AppProps;
import ru.butakov.survey.domain.Question;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ConditionalOnProperty(value = "app.repository", havingValue = "ymlRepository")
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Loggable
public class QuestionRepositoryYmlImpl implements QuestionRepository {
    ResourceLoader resourceLoader;
    AppProps appProps;

    @Override
    public List<Question> findAll() {
        InputStream in = resourceLoader.getInputStream(appProps.getFilename());
        Yaml yaml = new Yaml(new Constructor(Question.class));
        Iterable<Object> questions = yaml.loadAll(in);
        return StreamSupport.stream(questions.spliterator(), false)
                .map(q -> (Question) q)
                .collect(Collectors.toList());
    }

    @Override
    public Question findById(int id) {
        InputStream in = resourceLoader.getInputStream(appProps.getFilename());
        Yaml yaml = new Yaml(new Constructor(Question.class));
        Iterable<Object> questions = yaml.loadAll(in);
        return StreamSupport.stream(questions.spliterator(), false)
                .map(q -> (Question) q)
                .filter(q -> q.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
