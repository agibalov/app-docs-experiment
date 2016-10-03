package me.loki2302;

import me.loki2302.spring.TransactionComponent;
import org.springframework.stereotype.Service;

/**
 * @undocumented
 */
@Service
public class TwitterService {
    @TransactionComponent("Notify followers")
    public void notifyFollowers() {
    }
}
