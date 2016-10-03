package me.loki2302;

import me.loki2302.core.TransactionComponent;
import me.loki2302.core.TransactionEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

/**
 * @stereotype controller
 * @description Exposes a "notes" endpoint
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {
    /**
     * All note-related transactions
     */
    @Autowired
    private NoteService noteService;

    @Autowired
    private UserActivityEventService userActivityEventService;

    @Autowired
    private UserActivityEventFactory userActivityEventFactory;

    /**
     * Given all necessary note attributes, create a new note.
     * @param noteDto
     * @return
     */
    @TransactionEntryPoint("Create a note")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createNote(@RequestBody NoteDto noteDto) {
        long noteId = noteService.createNote(noteDto.text);
        URI location = fromMethodCall(on(NoteController.class).getNote(noteId)).build().toUri();

        UserActivityEvent event = userActivityEventFactory.makeNoteCreatedEvent(noteId);
        userActivityEventService.logUserActivityEvent(event);

        return ResponseEntity.created(location).build();
    }

    /**
     * Given a note ID, provide a note.
     * @param noteId
     * @return
     */
    @TransactionEntryPoint("Get a note")
    @RequestMapping(value = "{noteId}", method = RequestMethod.GET)
    public ResponseEntity getNote(@PathVariable long noteId) {
        Note note = noteService.getNote(noteId);
        if(note == null) {
            return ResponseEntity.notFound().build();
        }

        UserActivityEvent event = userActivityEventFactory.makeNoteRetrievedEvent(noteId);
        userActivityEventService.logUserActivityEvent(event);

        NoteWithIdDto noteWithIdDto = makeNoteWithIdDto(note);
        return ResponseEntity.ok(noteWithIdDto);
    }

    /**
     * Provide all notes.
     * @return
     */
    @TransactionEntryPoint("Get all notes")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getNotes() {
        List<Note> notes = noteService.getNotes();
        List<NoteWithIdDto> noteWithIdDtos = notes.stream()
                .map(NoteController::makeNoteWithIdDto)
                .collect(Collectors.toList());

        UserActivityEvent event = userActivityEventFactory.makeAllNotesRetrievedEvent();
        userActivityEventService.logUserActivityEvent(event);

        return ResponseEntity.ok(noteWithIdDtos);
    }

    /**
     * Given a note ID and all necessary attributes, update the note if it exists.
     * @param noteId
     * @param noteDto
     * @return
     */
    @TransactionEntryPoint("Update a note")
    @RequestMapping(value = "{noteId}", method = RequestMethod.PUT)
    public ResponseEntity updateNote(@PathVariable long noteId, @RequestBody NoteDto noteDto) {
        Note note = noteService.updateNote(noteId, noteDto.text);
        if(note == null) {
            return ResponseEntity.notFound().build();
        }

        UserActivityEvent event = userActivityEventFactory.makeNoteUpdatedEvent(noteId);
        userActivityEventService.logUserActivityEvent(event);

        return ResponseEntity.noContent().build();
    }

    /**
     * Given a note ID, delete a note.
     * @param noteId
     * @return
     */
    @TransactionEntryPoint("Delete a note")
    @RequestMapping(value = "{noteId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteNote(@PathVariable long noteId) {
        noteService.deleteNote(noteId);

        UserActivityEvent event = userActivityEventFactory.makeNoteDeletedEvent(noteId);
        userActivityEventService.logUserActivityEvent(event);

        return ResponseEntity.noContent().build();
    }

    private static NoteWithIdDto makeNoteWithIdDto(Note note) {
        NoteWithIdDto noteWithIdDto = new NoteWithIdDto();
        noteWithIdDto.id = note.id;
        noteWithIdDto.text = note.text;
        return noteWithIdDto;
    }
}
