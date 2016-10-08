package me.loki2302.services;

import me.loki2302.persistence.Note;
import me.loki2302.persistence.NoteRepository;
import me.loki2302.spring.TransactionComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service for Notes
 *
 * @stereotype service
 */
@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TwitterService twitterService;

    /**
     * Create a new note
     *
     * @param text a note text
     * @return a newly created note ID
     */
    @TransactionComponent("Create a note and provide ID")
    public long createNote(String text) {
        Note note = new Note();
        note.text = text;
        note = noteRepository.save(note);

        twitterService.notifyFollowers();

        return note.id;
    }

    /**
     * Get an existing note by ID
     *
     * @param noteId a note ID
     * @return an existing note, or null if it does not exist
     */
    @TransactionComponent("Get a note by ID")
    public Note getNote(long noteId) {
        return noteRepository.findOne(noteId);
    }

    /**
     * Get all notes
     *
     * @return a list of all existing notes
     */
    @TransactionComponent("Get all notes")
    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    /**
     * Update an existing note
     *
     * @param noteId an ID of note to update
     * @param text a new text to update the note with
     * @return an updated note, or null if requested note doesn't exist
     */
    @TransactionComponent("Update a note given ID and text")
    public Note updateNote(long noteId, String text) {
        Note note = noteRepository.findOne(noteId);
        if(note == null) {
            return null;
        }

        note.text = text;
        note = noteRepository.save(note);
        return note;
    }

    /**
     * Delete a note by ID
     *
     * @param noteId an ID of note to delete
     */
    @TransactionComponent("Delete a note by id")
    public void deleteNote(long noteId) {
        noteRepository.delete(noteId);
    }
}
