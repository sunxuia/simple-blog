package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.CollectionBO;
import net.sunxu.demo.sb.entity.Article;
import net.sunxu.demo.sb.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface  CollectionService {

    CollectionBO getCollection(Long collectionId);

    List<CollectionBO> listCollections(Long userId);

    void addCollection(CollectionBO collection);

    void updateCollection(CollectionBO collection);

    void deleteCollection(CollectionBO collectionId);

    void collectArticle(Long collectionId, Long articleId);

    void decollectArticle(Long collectionId, Long articleId);

    boolean isCollected(Long collectionId, Long articleId);

}
