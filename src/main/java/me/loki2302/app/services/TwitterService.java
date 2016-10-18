package me.loki2302.app.services;

import me.loki2302.spring.TransactionComponent;
import org.springframework.stereotype.Service;

/**
 * Twitter notification service
 *
 * @stereotype service
 */
@Service
public class TwitterService {
    /**
     * Notify Twitter followers about a very important event.
     */
    @TransactionComponent("Notify followers")
    public void notifyFollowers() {
    }
}
