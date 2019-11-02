package net.sunxu.demo.sb.service.event;

import net.sunxu.demo.sb.bo.ArticleBO;
import net.sunxu.demo.sb.entity.Article;
import org.springframework.context.ApplicationEvent;

public class ArticleEvent extends ApplicationEvent {

    private ArticleBO article;

    public ArticleEvent(Object source, ArticleBO article) {
        super(source);
        this.article = article;
    }

    public ArticleBO getArticle() {
        return article;
    }
}
