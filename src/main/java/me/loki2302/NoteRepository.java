package me.loki2302;

import me.loki2302.spring.TransactionComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * A repository for Notes
 *
 * @stereotype repository
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
    /**
     * @undocumented
     * @param entity
     * @return
     */
    @TransactionComponent("Save a note")
    @Override
    Note save(Note entity);

    /**
     * @undocumented
     * @param id
     * @return
     */
    @TransactionComponent("Find a note")
    @Override
    Note findOne(Long id);

    /**
     * @undocumented
     * @return
     */
    @TransactionComponent("Find all notes")
    @Override
    List<Note> findAll();

    /**
     * @undocumented
     * @param id
     */
    @TransactionComponent("Delete a note")
    @Override
    void delete(Long id);
}
