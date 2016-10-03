package me.loki2302;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.loki2302.core.SnippetWriter;
import me.loki2302.core.snippets.SequenceDiagramSnippet;
import me.loki2302.core.spring.TransactionScript;
import me.loki2302.core.spring.TransactionScriptGenerator;
import me.loki2302.core.spring.TransactionTraceConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        App.class,
        TransactionTraceConfiguration.class
}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionTest {
    @Rule
    public final SnippetWriter snippetWriter = new SnippetWriter(System.getProperty("snippetsDir"));

    @Autowired
    private TransactionScriptGenerator transactionScriptGenerator;

    @Test
    public void itShouldCreateANote() {
        RestTemplate restTemplate = new RestTemplate();

        NoteDto noteDto = new NoteDto();
        noteDto.text = "hello world";

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
                "http://localhost:8080/api/notes",
                noteDto,
                Void.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("http://localhost:8080/api/notes/1", responseEntity.getHeaders().getLocation().toString());

        TransactionScript transactionScript = transactionScriptGenerator.generateTransactionScript();
        snippetWriter.write("sequenceDiagram.puml", new SequenceDiagramSnippet(transactionScript.title, transactionScript.steps));
    }
}
