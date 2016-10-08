package me.loki2302.persistence;

import me.loki2302.spring.TransactionComponent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A repository for user activity events
 *
 * @stereotype repository
 */
public interface UserActivityEventRepository extends JpaRepository<UserActivityEvent, Long> {
    /**
     * Save an instance of UserActivityEvent
     * @param entity an event to save
     * @return a saved instance of original event
     */
    @TransactionComponent("Save user activity event")
    @Override
    UserActivityEvent save(UserActivityEvent entity);
}
