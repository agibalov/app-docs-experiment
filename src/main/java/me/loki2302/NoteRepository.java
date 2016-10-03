package me.loki2302;

import me.loki2302.core.TransactionComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @stereotype repository
 * @description A repository for Notes
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
    @TransactionComponent("Save a note")
    @Override
    Note save(Note entity);

    @TransactionComponent("Find a note")
    @Override
    Note findOne(Long id);

    @TransactionComponent("Find all notes")
    @Override
    List<Note> findAll();

    @TransactionComponent("Delete a note")
    @Override
    void delete(Long id);
}
