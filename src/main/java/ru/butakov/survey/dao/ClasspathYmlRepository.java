package ru.butakov.survey.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.butakov.survey.domain.Question;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ConditionalOnProperty(value = "app.repository", havingValue = "yamlRepository")
@Repository
public class ClasspathYmlRepository implements QuestionRepository {

    @Override
    public List<Question> findAll() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("questions.yml");
        Yaml yaml = new Yaml(new Constructor(Question.class));
        Iterable<Object> questions = yaml.loadAll(in);
        return StreamSupport.stream(questions.spliterator(), false)
                .map(q -> (Question) q)
                .collect(Collectors.toList());
    }

    @Override
    public Question findById(int id) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("questions.yml");
        Yaml yaml = new Yaml(new Constructor(Question.class));
        Iterable<Object> questions = yaml.loadAll(in);
        return StreamSupport.stream(questions.spliterator(), false)
                .map(q -> (Question) q)
                .filter(q -> q.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
