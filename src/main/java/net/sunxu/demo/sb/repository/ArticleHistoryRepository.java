package net.sunxu.demo.sb.repository;

import net.sunxu.demo.sb.entity.ArticleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleHistoryRepository extends JpaRepository<ArticleHistory, ArticleHistory.ArticleHistoryKey> {

    List<ArticleHistory> findAllByAndArticleIdOrderByTimeKey(Long articleId);

    void deleteAllByArticleId(Long articleId);
}
