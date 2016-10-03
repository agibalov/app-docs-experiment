package me.loki2302;

import me.loki2302.spring.TransactionComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @undocumented
 */
@Service
public class UserActivityEventService {
    @Autowired
    private UserActivityEventRepository userActivityEventRepository;

    @TransactionComponent("Log user activity event")
    public void logUserActivityEvent(UserActivityEvent userActivityEvent) {
        userActivityEventRepository.save(userActivityEvent);
    }
}
