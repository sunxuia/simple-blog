package net.sunxu.demo.sb.service.event;

import net.sunxu.demo.sb.bo.ArticleBO;
import net.sunxu.demo.sb.entity.Article;

public class ArticleDeletedEvent extends ArticleEvent {
    public ArticleDeletedEvent(Object source, ArticleBO article) {
        super(source, article);
    }
}
