package net.sunxu.demo.sb.service.event;

import net.sunxu.demo.sb.bo.UserBO;
import net.sunxu.demo.sb.entity.Collection;
import org.springframework.context.ApplicationEvent;

public class UserDeletedEvent extends ApplicationEvent {

    private UserBO user;

    public UserDeletedEvent(Object source, UserBO user) {
        super(source);
        this.user = this.user;
    }

    public UserBO getUser() {
        return user;
    }
}
