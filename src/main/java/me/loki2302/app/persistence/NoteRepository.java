package me.loki2302.app.persistence;

import me.loki2302.spring.TransactionComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * A repository for Notes.
 *
 * @stereotype repository
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
    /**
     * Save or update a note.
     *
     * @param entity note to be saved
     * @return saved or updated not instance
     */
    @TransactionComponent("Save a note")
    @Override
    Note save(Note entity);

    /**
     * Find a note by ID.
     *
     * @param id a note ID
     * @return a note with given ID or null
     */
    @TransactionComponent("Find a note")
    @Override
    Note findOne(Long id);

    /**
     * Find all notes.
     *
     * @return a list of notes
     */
    @TransactionComponent("Find all notes")
    @Override
    List<Note> findAll();

    /**
     * Delete a note by ID.
     *
     * @param id a note ID
     */
    @TransactionComponent("Delete a note")
    @Override
    void delete(Long id);
}
