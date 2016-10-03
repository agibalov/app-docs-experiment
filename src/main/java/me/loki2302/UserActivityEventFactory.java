package me.loki2302;

import me.loki2302.spring.TransactionComponent;
import org.springframework.stereotype.Component;

/**
 * @undocumented
 */
@Component
public class UserActivityEventFactory {
    @TransactionComponent("Construct a \"note created\" event")
    public UserActivityEvent makeNoteCreatedEvent(long id) {
        return makeUserActivityEvent("User created a note %d", id);
    }

    @TransactionComponent("Construct a \"note updated\" event")
    public UserActivityEvent makeNoteUpdatedEvent(long id) {
        return makeUserActivityEvent("User updated a note %d", id);
    }

    @TransactionComponent("Construct a \"note deleted\" event")
    public UserActivityEvent makeNoteDeletedEvent(long id) {
        return makeUserActivityEvent("User deleted a note %d", id);
    }

    @TransactionComponent("Construct a \"note retrieved\" event")
    public UserActivityEvent makeNoteRetrievedEvent(long id) {
        return makeUserActivityEvent("User retrieved a note %d", id);
    }

    @TransactionComponent("Construct an \"all notes retrieved\" event")
    public UserActivityEvent makeAllNotesRetrievedEvent() {
        return makeUserActivityEvent("User retrieved all notes");
    }

    private static UserActivityEvent makeUserActivityEvent(String format, Object... args) {
        UserActivityEvent userActivityEvent = new UserActivityEvent();
        userActivityEvent.text = String.format(format, args);
        return userActivityEvent;
    }
}
