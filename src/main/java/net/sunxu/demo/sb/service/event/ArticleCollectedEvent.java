package net.sunxu.demo.sb.service.event;

import org.springframework.context.ApplicationEvent;

public class ArticleCollectedEvent extends ApplicationEvent {
    private Long articleId, collectionId;

    private boolean isCollect;

    public ArticleCollectedEvent(Object source, Long collectionId, Long articleId, boolean isCollect) {
        super(source);
        this.articleId = articleId;
        this.collectionId = collectionId;
        this.isCollect = isCollect;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public boolean getCollect() {
        return isCollect;
    }
}
