package me.loki2302.app;

import me.loki2302.app.dtos.NoteDto;
import me.loki2302.app.dtos.NoteWithIdDto;
import me.loki2302.app.services.NoteService;
import me.loki2302.documentation.SnippetWriter;
import me.loki2302.spring.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        App.class,
        TransactionTest.Config.class
}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionTest {
    @Rule
    public final SnippetWriter snippetWriter = new SnippetWriter(System.getProperty("snippetsDir"));

    @Autowired
    private TransactionRecorder transactionRecorder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NoteService noteService;

    @Test
    public void documentCreateNote() {
        NoteDto noteDto = new NoteDto();
        noteDto.text = "hello world";

        transactionRecorder.resetTransaction();

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
                "http://localhost:8080/api/notes",
                noteDto,
                Void.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("http://localhost:8080/api/notes/1", responseEntity.getHeaders().getLocation().toString());

        List<TransactionEvent> transactionEvents = transactionRecorder.getTransactionEvents();
        TransactionFrame rootTransactionFrame = TransactionFrameBuilder.build(transactionEvents);
        snippetWriter.write("sequenceDiagram.puml", new SequenceDiagramSnippet(rootTransactionFrame));
    }

    @Test
    public void documentUpdateNote() {
        long noteId = noteService.createNote("hello world");

        NoteDto noteDto = new NoteDto();
        noteDto.text = "updated hello world";

        transactionRecorder.resetTransaction();

        restTemplate.put("http://localhost:8080/api/notes/{id}", noteDto, noteId);

        List<TransactionEvent> transactionEvents = transactionRecorder.getTransactionEvents();
        TransactionFrame rootTransactionFrame = TransactionFrameBuilder.build(transactionEvents);
        snippetWriter.write("sequenceDiagram.puml", new SequenceDiagramSnippet(rootTransactionFrame));
    }

    @Test
    public void documentDeleteNote() {
        long noteId = noteService.createNote("hello world");

        transactionRecorder.resetTransaction();

        restTemplate.delete("http://localhost:8080/api/notes/{id}", noteId);

        List<TransactionEvent> transactionEvents = transactionRecorder.getTransactionEvents();
        TransactionFrame rootTransactionFrame = TransactionFrameBuilder.build(transactionEvents);
        snippetWriter.write("sequenceDiagram.puml", new SequenceDiagramSnippet(rootTransactionFrame));
    }

    @Test
    public void documentGetNote() {
        long noteId = noteService.createNote("hello world");

        transactionRecorder.resetTransaction();

        ResponseEntity<NoteWithIdDto> responseEntity = restTemplate.getForEntity(
                "http://localhost:8080/api/notes/{id}",
                NoteWithIdDto.class,
                noteId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody().id);
        assertEquals("hello world", responseEntity.getBody().text);

        List<TransactionEvent> transactionEvents = transactionRecorder.getTransactionEvents();
        TransactionFrame rootTransactionFrame = TransactionFrameBuilder.build(transactionEvents);
        snippetWriter.write("sequenceDiagram.puml", new SequenceDiagramSnippet(rootTransactionFrame));
    }

    @Test
    public void documentGetAllNotes() {
        long noteId1 = noteService.createNote("hello world #1");
        long noteId2 = noteService.createNote("hello world #2");
        long noteId3 = noteService.createNote("hello world #3");

        transactionRecorder.resetTransaction();

        ResponseEntity<List<NoteWithIdDto>> responseEntity = restTemplate.exchange(
                "http://localhost:8080/api/notes",
                HttpMethod.GET,
                RequestEntity.EMPTY,
                new ParameterizedTypeReference<List<NoteWithIdDto>>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3, responseEntity.getBody().size());
        assertTrue(responseEntity.getBody().stream().anyMatch(n -> n.id == noteId1 && n.text.equals("hello world #1")));
        assertTrue(responseEntity.getBody().stream().anyMatch(n -> n.id == noteId2 && n.text.equals("hello world #2")));
        assertTrue(responseEntity.getBody().stream().anyMatch(n -> n.id == noteId3 && n.text.equals("hello world #3")));

        List<TransactionEvent> transactionEvents = transactionRecorder.getTransactionEvents();
        TransactionFrame rootTransactionFrame = TransactionFrameBuilder.build(transactionEvents);
        snippetWriter.write("sequenceDiagram.puml", new SequenceDiagramSnippet(rootTransactionFrame));
    }

    @Configuration
    @EnableTransactionTracing
    public static class Config {
        @Bean
        public RestTemplate restTemplate() {
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            return new RestTemplate(requestFactory);
        }
    }
}
