package me.loki2302;

import me.loki2302.spring.TransactionComponent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @stereotype repository
 * @description A repository for user activity events
 */
public interface UserActivityEventRepository extends JpaRepository<UserActivityEvent, Long> {
    @TransactionComponent("Save user activity event")
    @Override
    UserActivityEvent save(UserActivityEvent entity);
}
