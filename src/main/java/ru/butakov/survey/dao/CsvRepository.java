package ru.butakov.survey.dao;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import ru.butakov.survey.aop.Loggable;
import ru.butakov.survey.config.AppProps;
import ru.butakov.survey.domain.Answer;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Loggable
@ConditionalOnProperty(value = "app.repository", havingValue = "csvRepository")
@AllArgsConstructor
@Repository
public class CsvRepository implements QuestionRepository {
    AppProps appProps;
    ResourceLoader resourceLoader;

    @Override
    public List<Question> findAll() {
        Map<Integer, Question> map = getMapFromCsv(appProps.getFilename());
        return new ArrayList<>(map.values());
    }

    @Override
    public Question findById(int id) {
        Map<Integer, Question> map = getMapFromCsv(appProps.getFilename());
        return map.get(id);
    }

    Map<Integer, Question> getMapFromCsv(String filename) {
        InputStream inputStream = resourceLoader.getInputStream(filename);
        try (inputStream) {
            Reader in = new InputStreamReader(inputStream);
            Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            return getQuestionMap(csvRecords);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    Map<Integer, Question> getQuestionMap(Iterable<CSVRecord> csvRecords) {
        Map<Integer, Question> questionMap = new HashMap<>();
        for (CSVRecord record : csvRecords) {
            String type = record.get("type");
            switch (type) {
                case "Q":
                    addQuestionFromRecordToMap(questionMap, record);
                    break;
                case "A":
                    addAnswerFromRecordToQuestion(questionMap, record);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Type can be 'Q' or 'A' only, not %s", type));
            }
        }
        return questionMap;
    }

    void addQuestionFromRecordToMap(Map<Integer, Question> questionMap, CSVRecord record) {
        int id = Integer.parseInt(record.get("id"));
        int points = Integer.parseInt(record.get("points"));
        QuestionType type = QuestionType.valueOf(record.get("typeQ"));
        String text = record.get("text");

        if (questionMap.containsKey(id))
            throw new IllegalArgumentException("Duplicate question with number " + id);

        Question question = new Question(id, type, text, points);
        questionMap.put(id, question);
    }

    void addAnswerFromRecordToQuestion(Map<Integer, Question> questionMap, CSVRecord record) {
        int id = Integer.parseInt(record.get("id"));
        String text = record.get("text");
        boolean flag = record.get("right").equals("true");

        Question question = questionMap.get(id);
        if (question == null)
            throw new IllegalArgumentException(String.format("Question with number %d not found for answer", id));

        Answer answer = new Answer(question, text, flag);
        question.addAnswer(answer);
    }
}
