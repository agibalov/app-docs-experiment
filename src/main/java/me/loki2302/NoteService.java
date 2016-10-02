package me.loki2302;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public long createNote(String text) {
        Note note = new Note();
        note.text = text;
        note = noteRepository.save(note);
        return note.id;
    }

    public Note getNote(long noteId) {
        return noteRepository.findOne(noteId);
    }

    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    public Note updateNote(long noteId, String text) {
        Note note = noteRepository.findOne(noteId);
        if(note == null) {
            return null;
        }

        note.text = text;
        note = noteRepository.save(note);
        return note;
    }

    public void deleteNote(long noteId) {
        noteRepository.delete(noteId);
    }
}
