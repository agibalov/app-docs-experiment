package me.loki2302.app.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @undocumented
 */
@Entity
public class Note {
    @Id
    @GeneratedValue
    public Long id;
    public String text;
}
