package me.loki2302;

import me.loki2302.spring.TransactionComponent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A repository for user activity events
 *
 * @stereotype repository
 */
public interface UserActivityEventRepository extends JpaRepository<UserActivityEvent, Long> {
    /**
     * @undocumented
     * @param entity
     * @return
     */
    @TransactionComponent("Save user activity event")
    @Override
    UserActivityEvent save(UserActivityEvent entity);
}
