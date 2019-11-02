package net.sunxu.demo.sb.service.event;

import net.sunxu.demo.sb.bo.CollectionBO;
import net.sunxu.demo.sb.entity.Collection;
import org.springframework.context.ApplicationEvent;

public class CollectionDeletedEvent extends ApplicationEvent {

    private CollectionBO collection;

    public CollectionDeletedEvent(Object source, CollectionBO collection) {
        super(source);
        this.collection = collection;
    }

    public CollectionBO getCollection() {
        return collection;
    }
}
