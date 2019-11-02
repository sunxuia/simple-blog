package net.sunxu.demo.sb.repository;

import net.sunxu.demo.sb.entity.Article;
import net.sunxu.demo.sb.entity.ArticleStatus;
import net.sunxu.demo.sb.entity.User;
import net.sunxu.demo.sb.entity.ArticleCollect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findByUserAndStatus(User user, ArticleStatus status);

    void deleteAllByCategory(Long categoryId);

    Page<Article> findAllByCategory(Long categoryId, Pageable page);

    @Query("select id from Article where category.id = ?1")
    List<Long> listIdByCategoryId(Long categoryId);

    @Query("select a" +
            " from Article a, ArticleCollect c " +
            "where a.id = c.articleId " +
            "  and c.collectionId = ?1")
    Page<Article> findArticleByCollectionId(Long collectionId, Pageable page);

    @Query("update Article set status = ?2 where id = ?1")
    void updateArticleStatus(Long articleId, ArticleStatus articleStatus);
}
