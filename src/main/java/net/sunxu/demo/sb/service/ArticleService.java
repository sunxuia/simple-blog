package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.ArticleBO;
import net.sunxu.demo.sb.bo.ArticleEditBO;
import net.sunxu.demo.sb.entity.ArticleHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface ArticleService {
    Page<ArticleBO> listArticles(Date lastRefreshTime, int count);

    Page<ArticleBO> listArticles(Pageable option);

    ArticleBO getArticle(Long articleId);

    void likeArticle(ArticleBO article);

    void dislikeArticle(ArticleBO article);

    void lockArticle(ArticleBO article);

    void unlockArticle(ArticleBO article);

    ArticleEditBO createArticle();

    ArticleEditBO editArticle(ArticleBO article);

    void deleteArticle(ArticleBO article);

    void saveDraft(ArticleEditBO article);

    void releaseArticle(ArticleEditBO article);

    List<ArticleHistory> listArticleHistory(ArticleBO article);

    Page<ArticleBO> searchArticle(SearchType type, String text, Pageable option);

    Page<ArticleBO> rankArticle(ArticleRankType type, Pageable option);

    Page<ArticleBO> listCategoryArticles(Long categoryId, Pageable option);

    Page<ArticleBO> listCollectionArticles(Long collectionId, Pageable option);
}
