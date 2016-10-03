package me.loki2302;

import me.loki2302.spring.TransactionComponent;
import org.springframework.stereotype.Service;

/**
 * @stereotype service
 * @description Twitter notification service
 */
@Service
public class TwitterService {
    @TransactionComponent("Notify followers")
    public void notifyFollowers() {
    }
}
