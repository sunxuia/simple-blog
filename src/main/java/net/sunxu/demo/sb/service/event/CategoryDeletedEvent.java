package net.sunxu.demo.sb.service.event;

import net.sunxu.demo.sb.bo.CategoryBO;
import net.sunxu.demo.sb.entity.Category;
import org.springframework.context.ApplicationEvent;

public class CategoryDeletedEvent extends ApplicationEvent {

    private CategoryBO category;

    public CategoryDeletedEvent(Object source, CategoryBO category) {
        super(source);
        this.category = category;
    }

    public CategoryBO getCategory() {
        return category;
    }
}
