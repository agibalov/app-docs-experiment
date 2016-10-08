package me.loki2302;

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
     * @undocumented
     */
    @TransactionComponent("Notify followers")
    public void notifyFollowers() {
    }
}
