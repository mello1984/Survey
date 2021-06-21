package ru.butakov.survey.dao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.butakov.survey.domain.Answer;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ClasspathCsvRepositoryTest {
    @Spy
    private final ClasspathCsvRepository classpathCsvRepository = new ClasspathCsvRepository("");

    @Test
    void findAll() {
        Question question = new Question(1, QuestionType.RADIO_BOX, "Question text", 10);
        Map<Integer, Question> map = Map.of(question.getId(), question);
        List<Question> expected = List.of(question);

        Mockito.when(classpathCsvRepository.getMapFromCsv("")).thenReturn(map);
        List<Question> actual = classpathCsvRepository.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getMapFromCsv_thenSuccessful() {
        Mockito.doReturn(Collections.emptyMap()).when(classpathCsvRepository).putRecordsToMap(Mockito.any());
        String filename = "";
        classpathCsvRepository.getMapFromCsv(filename);
        Mockito.verify(classpathCsvRepository).putRecordsToMap(Mockito.any());
    }

    @Test
    void getMapFromCsv_thenFailed_notFound() {
        String filename = "thisFileNotExists.file";
        assertThatThrownBy(() -> classpathCsvRepository.getMapFromCsv(filename))
                .isInstanceOf(UncheckedIOException.class)
                .hasMessageContaining(String.format("File '%s' not found on classpath", filename));
        Mockito.verify(classpathCsvRepository).getMapFromCsv(filename);
        Mockito.verifyNoMoreInteractions(classpathCsvRepository);
    }

    @Test
    void putRecordsToMap_thenAddQuestion() throws IOException {
        String csvString = "id,type,typeQ,text,right,points\n1,Q,RADIO_BOX,Question text,,10";
        StringReader reader = new StringReader(csvString);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        Mockito.doNothing().when(classpathCsvRepository).addQuestionFromRecordToMap(Mockito.anyMap(), Mockito.any(CSVRecord.class));

        classpathCsvRepository.putRecordsToMap(records);

        Mockito.verify(classpathCsvRepository).addQuestionFromRecordToMap(Mockito.anyMap(), Mockito.any(CSVRecord.class));
        Mockito.verify(classpathCsvRepository).putRecordsToMap(records);
        Mockito.verifyNoMoreInteractions(classpathCsvRepository);
    }

    @Test
    void putRecordsToMap_thenAddAnswer() throws IOException {
        String csvString = "id,type,typeQ,text,right,points\n1,A,,Answer text,true,";
        StringReader reader = new StringReader(csvString);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        Mockito.doNothing().when(classpathCsvRepository).addAnswerFromRecordToQuestion(Mockito.anyMap(), Mockito.any(CSVRecord.class));

        classpathCsvRepository.putRecordsToMap(records);

        Mockito.verify(classpathCsvRepository).addAnswerFromRecordToQuestion(Mockito.anyMap(), Mockito.any(CSVRecord.class));
        Mockito.verify(classpathCsvRepository).putRecordsToMap(records);
        Mockito.verifyNoMoreInteractions(classpathCsvRepository);
    }

    @Test
    void putRecordsToMap_thenFailed_wrongType() throws IOException {
        String csvString = "id,type,typeQ,text,right,points\n1,W,,Answer text,true,";
        StringReader reader = new StringReader(csvString);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        assertThatThrownBy(() -> classpathCsvRepository.putRecordsToMap(records))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Type can be 'Q' or 'A' only, not W");

        Mockito.verify(classpathCsvRepository).putRecordsToMap(records);
        Mockito.verifyNoMoreInteractions(classpathCsvRepository);
    }

    @Test
    void addQuestion_thenSuccessful() throws IOException {
        String csvString = "id,type,typeQ,text,right,points\n1,Q,RADIO_BOX,Question text,,10";
        StringReader reader = new StringReader(csvString);

        Question question = new Question(1, QuestionType.RADIO_BOX, "Question text", 10);
        CSVRecord record = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(reader).getRecords().get(0);

        Map<Integer, Question> expected = Map.of(question.getId(), question);
        Map<Integer, Question> actual = new HashMap<>();

        classpathCsvRepository.addQuestionFromRecordToMap(actual, record);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addQuestion_thenFailed_duplicateKey() throws IOException {
        String csvString = "id,type,typeQ,text,right,points\n1,Q,RADIO_BOX,Question text,,10";
        StringReader reader = new StringReader(csvString);

        Question question = new Question(1, QuestionType.RADIO_BOX, "Question text", 10);
        CSVRecord record = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(reader).getRecords().get(0);

        Map<Integer, Question> expected = Map.of(question.getId(), question);
        Map<Integer, Question> actual = new HashMap<>(expected);

        assertThatThrownBy(() -> classpathCsvRepository.addQuestionFromRecordToMap(actual, record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duplicate question with number 1");
    }

    @Test
    void addAnswer_thenSuccessful() throws IOException {
        String csvString = "id,type,typeQ,text,right,points\n1,A,,Answer text,true,";
        StringReader reader = new StringReader(csvString);

        Question question = new Question(1, QuestionType.RADIO_BOX, "Question text", 10);
        Answer answer = new Answer(question, "Answer text", true);
        question.addAnswer(answer);

        CSVRecord record = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(reader).getRecords().get(0);

        Map<Integer, Question> expected = Map.of(question.getId(), question);
        Map<Integer, Question> actual = new HashMap<>(expected);

        classpathCsvRepository.addAnswerFromRecordToQuestion(actual, record);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addAnswer_thenFailed_NotExists() throws IOException {
        String csvString = "id,type,typeQ,text,right,points\n1,A,,Answer text,true,";
        StringReader reader = new StringReader(csvString);

        Question question = new Question(4, QuestionType.RADIO_BOX, "Question text", 10);
        Answer answer = new Answer(question, "Answer text", true);
        question.addAnswer(answer);

        CSVRecord record = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(reader).getRecords().get(0);

        Map<Integer, Question> expected = Map.of(question.getId(), question);
        Map<Integer, Question> actual = new HashMap<>(expected);

        assertThatThrownBy(() -> classpathCsvRepository.addAnswerFromRecordToQuestion(actual, record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Question with number 1 not found for answer");
    }

    @Test
    void findById() {
        int id = 1;
        Question expected = new Question(id, QuestionType.RADIO_BOX, "text", 10);
        Mockito.when(classpathCsvRepository.getMapFromCsv("")).thenReturn(Map.of(expected.getId(), expected));
        Question actual = classpathCsvRepository.findById(id);
        assertThat(actual).isEqualTo(expected);
    }
}