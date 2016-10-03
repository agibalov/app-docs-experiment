package me.loki2302;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @undocumented
 */
@Entity
public class UserActivityEvent {
    @Id
    @GeneratedValue
    public Long id;
    public String text;
}
