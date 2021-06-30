package ru.butakov.survey.dao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.butakov.survey.config.AppProps;
import ru.butakov.survey.domain.Answer;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;
import ru.butakov.survey.exceptions.SurveyDaoException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class QuestionRepositoryCsvImplTest {
    @Mock
    AppProps appProps;
    @Mock
    ResourceLoader resourceLoader;

    @Test
    void findAll() {
        Question question = new Question(1, QuestionType.RADIO_BOX, "Question text", 10);
        Map<Integer, Question> map = Map.of(question.getId(), question);
        List<Question> expected = List.of(question);

        QuestionRepositoryCsvImpl csvRepository = Mockito.spy(new QuestionRepositoryCsvImpl(appProps, resourceLoader));
        String filename = "filename";
        Mockito.when(appProps.getFilename()).thenReturn(filename);
        Mockito.doReturn(map).when(csvRepository).getMapFromCsv(filename);

        List<Question> actual = csvRepository.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findById() {
        int id = 1;
        Question expected = new Question(id, QuestionType.RADIO_BOX, "text", 10);
        Map<Integer, Question> map = Map.of(expected.getId(), expected);

        QuestionRepositoryCsvImpl csvRepository = Mockito.spy(new QuestionRepositoryCsvImpl(appProps, resourceLoader));
        String filename = "filename";
        Mockito.when(appProps.getFilename()).thenReturn(filename);
        Mockito.doReturn(map).when(csvRepository).getMapFromCsv(filename);

        Question actual = csvRepository.findById(id);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getMapFromCsv_thenSuccessful() {
        Question question = new Question(1, QuestionType.RADIO_BOX, "Question text", 10);
        Map<Integer, Question> expected = Map.of(question.getId(), question);

        QuestionRepositoryCsvImpl csvRepository = Mockito.spy(new QuestionRepositoryCsvImpl(appProps, resourceLoader));
        String filename = "filename";
        InputStream stream = InputStream.nullInputStream();
        Mockito.when(resourceLoader.getInputStream(filename)).thenReturn(stream);
        Mockito.doReturn(expected).when(csvRepository).getQuestionMap(Mockito.any());

        Map<Integer, Question> actual = csvRepository.getMapFromCsv(filename);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void putRecordsToMap_thenAddQuestion() throws IOException {
        QuestionRepositoryCsvImpl csvRepository = Mockito.spy(new QuestionRepositoryCsvImpl(appProps, resourceLoader));
        String csvString = "id,type,typeQ,text,right,points\n1,Q,RADIO_BOX,Question text,,10";
        StringReader reader = new StringReader(csvString);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        Mockito.doNothing().when(csvRepository).addQuestionFromRecordToMap(Mockito.anyMap(), Mockito.any(CSVRecord.class));

        csvRepository.getQuestionMap(records);

        Mockito.verify(csvRepository).addQuestionFromRecordToMap(Mockito.anyMap(), Mockito.any(CSVRecord.class));
        Mockito.verify(csvRepository).getQuestionMap(records);
        Mockito.verifyNoMoreInteractions(csvRepository);
    }

    @Test
    void putRecordsToMap_thenAddAnswer() throws IOException {
        QuestionRepositoryCsvImpl csvRepository = Mockito.spy(new QuestionRepositoryCsvImpl(appProps, resourceLoader));
        String csvString = "id,type,typeQ,text,right,points\n1,A,,Answer text,true,";
        StringReader reader = new StringReader(csvString);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        Mockito.doNothing().when(csvRepository).addAnswerFromRecordToQuestion(Mockito.anyMap(), Mockito.any(CSVRecord.class));

        csvRepository.getQuestionMap(records);

        Mockito.verify(csvRepository).addAnswerFromRecordToQuestion(Mockito.anyMap(), Mockito.any(CSVRecord.class));
        Mockito.verify(csvRepository).getQuestionMap(records);
        Mockito.verifyNoMoreInteractions(csvRepository);
    }

    @Test
    void putRecordsToMap_thenFailed_wrongType() throws IOException {
        QuestionRepositoryCsvImpl csvRepository = Mockito.spy(new QuestionRepositoryCsvImpl(appProps, resourceLoader));
        String csvString = "id,type,typeQ,text,right,points\n1,W,,Answer text,true,";
        StringReader reader = new StringReader(csvString);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        assertThatThrownBy(() -> csvRepository.getQuestionMap(records))
                .isInstanceOf(SurveyDaoException.class)
                .hasMessage("Type can be 'Q' or 'A' only, not W");

        Mockito.verify(csvRepository).getQuestionMap(records);
        Mockito.verifyNoMoreInteractions(csvRepository);
    }

    @Test
    void addQuestion_thenSuccessful() throws IOException {
        QuestionRepositoryCsvImpl csvRepository = Mockito.spy(new QuestionRepositoryCsvImpl(appProps, resourceLoader));
        String csvString = "id,type,typeQ,text,right,points\n1,Q,RADIO_BOX,Question text,,10";
        StringReader reader = new StringReader(csvString);

        Question question = new Question(1, QuestionType.RADIO_BOX, "Question text", 10);
        CSVRecord record = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(reader).getRecords().get(0);

        Map<Integer, Question> expected = Map.of(question.getId(), question);
        Map<Integer, Question> actual = new HashMap<>();

        csvRepository.addQuestionFromRecordToMap(actual, record);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addQuestion_thenFailed_duplicateKey() throws IOException {
        QuestionRepositoryCsvImpl csvRepository = Mockito.spy(new QuestionRepositoryCsvImpl(appProps, resourceLoader));
        String csvString = "id,type,typeQ,text,right,points\n1,Q,RADIO_BOX,Question text,,10";
        StringReader reader = new StringReader(csvString);

        Question question = new Question(1, QuestionType.RADIO_BOX, "Question text", 10);
        CSVRecord record = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(reader).getRecords().get(0);

        Map<Integer, Question> expected = Map.of(question.getId(), question);
        Map<Integer, Question> actual = new HashMap<>(expected);

        assertThatThrownBy(() -> csvRepository.addQuestionFromRecordToMap(actual, record))
                .isInstanceOf(SurveyDaoException.class)
                .hasMessage("Duplicate question with number 1");
    }

    @Test
    void addAnswer_thenSuccessful() throws IOException {
        QuestionRepositoryCsvImpl csvRepository = Mockito.spy(new QuestionRepositoryCsvImpl(appProps, resourceLoader));
        String csvString = "id,type,typeQ,text,right,points\n1,A,,Answer text,true,";
        StringReader reader = new StringReader(csvString);

        Question question = new Question(1, QuestionType.RADIO_BOX, "Question text", 10);
        Answer answer = new Answer(question, "Answer text", true);
        question.addAnswer(answer);

        CSVRecord record = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(reader).getRecords().get(0);

        Map<Integer, Question> expected = Map.of(question.getId(), question);
        Map<Integer, Question> actual = new HashMap<>(expected);

        csvRepository.addAnswerFromRecordToQuestion(actual, record);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addAnswer_thenFailed_NotExists() throws IOException {
        QuestionRepositoryCsvImpl csvRepository = Mockito.spy(new QuestionRepositoryCsvImpl(appProps, resourceLoader));
        String csvString = "id,type,typeQ,text,right,points\n1,A,,Answer text,true,";
        StringReader reader = new StringReader(csvString);

        Question question = new Question(4, QuestionType.RADIO_BOX, "Question text", 10);
        Answer answer = new Answer(question, "Answer text", true);
        question.addAnswer(answer);

        CSVRecord record = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(reader).getRecords().get(0);

        Map<Integer, Question> expected = Map.of(question.getId(), question);
        Map<Integer, Question> actual = new HashMap<>(expected);

        assertThatThrownBy(() -> csvRepository.addAnswerFromRecordToQuestion(actual, record))
                .isInstanceOf(SurveyDaoException.class)
                .hasMessage("Question with number 1 not found for answer");
    }
}
