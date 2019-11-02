package net.sunxu.demo.sb.repository;

import net.sunxu.demo.sb.entity.ArticleCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ArticleCollectRepository extends JpaRepository<ArticleCollect, ArticleCollect.ArticleCollectKey> {
    void deleteByCollectionIdAndArticleId(Long collectionId, Long articleId);

    boolean existsByArticleIdAndCollectionId(Long articleId, Long collectionId);
}
