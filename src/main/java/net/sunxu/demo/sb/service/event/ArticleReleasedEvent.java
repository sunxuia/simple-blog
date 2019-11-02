package net.sunxu.demo.sb.service.event;

import net.sunxu.demo.sb.bo.ArticleBO;
import net.sunxu.demo.sb.entity.Article;

public class ArticleReleasedEvent extends ArticleEvent {
    public ArticleReleasedEvent(Object source, ArticleBO article) {
        super(source, article);
    }
}
