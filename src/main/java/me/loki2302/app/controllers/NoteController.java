package me.loki2302.app.controllers;

import me.loki2302.app.dtos.NoteDto;
import me.loki2302.app.dtos.NoteWithIdDto;
import me.loki2302.app.persistence.Note;
import me.loki2302.app.persistence.UserActivityEvent;
import me.loki2302.app. services.NoteService;
import me.loki2302.app.services.UserActivityEventFactory;
import me.loki2302.app.services.UserActivityEventService;
import me.loki2302.spring.TransactionEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

/**
 * Implements a "notes" REST resource. Provides functionality to create, retrieve, update and delete notes.
 *
 * @stereotype controller
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private UserActivityEventService userActivityEventService;

    @Autowired
    private UserActivityEventFactory userActivityEventFactory;

    /**
     * Given all necessary note attributes, create a new note.
     *
     * @param noteDto an object containing note attributes
     * @return a 201 response with Location header
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
     *
     * @param noteId a note ID
     * @return a 200 response with note attributes
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
     *
     * @return a collection of all notes
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
     *
     * @param noteId a note ID
     * @param noteDto an object containing note attributes
     * @return a 204 response
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
     *
     * @param noteId a note ID
     * @return a 204 response
     */
    @TransactionEntryPoint("Delete a note")
    @RequestMapping(value = "{noteId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteNote(@PathVariable long noteId) {
        noteService.deleteNote(noteId);

        UserActivityEvent event = userActivityEventFactory.makeNoteDeletedEvent(noteId);
        userActivityEventService.logUserActivityEvent(event);

        return ResponseEntity.noContent().build();
    }

    /**
     * @undocumented
     * @param note
     * @return
     */
    private static NoteWithIdDto makeNoteWithIdDto(Note note) {
        NoteWithIdDto noteWithIdDto = new NoteWithIdDto();
        noteWithIdDto.id = note.id;
        noteWithIdDto.text = note.text;
        return noteWithIdDto;
    }
}
