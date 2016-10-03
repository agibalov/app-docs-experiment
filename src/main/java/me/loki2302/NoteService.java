package me.loki2302;

import me.loki2302.core.TransactionComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @stereotype service
 * @description A service for Notes
 */
@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TwitterService twitterService;

    @TransactionComponent("Create a note and provide ID")
    public long createNote(String text) {
        Note note = new Note();
        note.text = text;
        note = noteRepository.save(note);

        twitterService.notifyFollowers();

        return note.id;
    }

    @TransactionComponent("Get a note by ID")
    public Note getNote(long noteId) {
        return noteRepository.findOne(noteId);
    }

    @TransactionComponent("Get all notes")
    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

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

    @TransactionComponent("Delete a note by id")
    public void deleteNote(long noteId) {
        noteRepository.delete(noteId);
    }
}
