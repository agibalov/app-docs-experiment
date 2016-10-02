package me.loki2302;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @stereotype repository
 * @description A repository for Notes
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
}
