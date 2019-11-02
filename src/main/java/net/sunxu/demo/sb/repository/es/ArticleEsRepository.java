package net.sunxu.demo.sb.repository.es;

import net.sunxu.demo.sb.entity.es.ArticleEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleEsRepository extends ElasticsearchRepository<ArticleEs, String> {

    Page<Long> findArticleIdByTitleContainingOrAbstractTextContainingOrContentTextContaining(
            String text, Pageable pageable);

    Page<Long> findArticleIdByTitleContaining(String text, Pageable pageable);

    Page<Long> findArticleIdByContentTextContainingOrAbstractTextContaining(String text, Pageable pageable);

    void deleteByArticleId(Long articleId);
}


