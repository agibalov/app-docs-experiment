package me.loki2302;

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
