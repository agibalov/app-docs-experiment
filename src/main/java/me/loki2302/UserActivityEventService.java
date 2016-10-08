package me.loki2302;

import me.loki2302.spring.TransactionComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service that handles user activity events
 *
 * @stereotype service
 */
@Service
public class UserActivityEventService {
    @Autowired
    private UserActivityEventRepository userActivityEventRepository;

    /**
     * @undocumented
     * @param userActivityEvent
     */
    @TransactionComponent("Log user activity event")
    public void logUserActivityEvent(UserActivityEvent userActivityEvent) {
        userActivityEventRepository.save(userActivityEvent);
    }
}
