package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.ArticleBO;
import net.sunxu.demo.sb.bo.ArticleEditBO;
import net.sunxu.demo.sb.bo.UserBO;
import net.sunxu.demo.sb.entity.*;
import net.sunxu.demo.sb.entity.es.ArticleEs;
import net.sunxu.demo.sb.exception.SimpleBlogException;
import net.sunxu.demo.sb.repository.*;
import net.sunxu.demo.sb.repository.es.ArticleEsRepository;
import net.sunxu.demo.sb.service.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static net.sunxu.demo.sb.util.SbObjectUtils.convertPage;
import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;
import static net.sunxu.demo.sb.util.SbWebUtils.getPrincipal;

@Service
public class ArticleServiceImpl implements ArticleService, ApplicationEventPublisherAware {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleDraftRepository articleDraftRepository;
    @Autowired
    private ArticleHistoryRepository articleHistoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RelationRedisService relationRedisService;
    @Autowired
    private CountRedisService countRedisService;
    @Autowired
    private ArticleEsRepository articleEsRepository;

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    public Page<ArticleBO> listArticles(Date lastRefreshTime, int count) {
        return convertPage(articleRepository.findAll(PageRequest.of(1, count)), this::convertArticle);
    }

    @Override
    public Page<ArticleBO> listArticles(Pageable option) {
        return convertPage(articleRepository.findAll(option), this::convertArticle);
    }

    private ArticleBO convertArticle(Article article) {
        ArticleBO bo = copyBean(article, new ArticleBO());
        UserBO author = copyBean(article.getUser(), new UserBO());
        bo.setAuthor(author);

        bo.setLikeCount(relationRedisService.getCount("article.like", article.getId()));
        bo.setLiked(relationRedisService.exist("article.like", article.getId(), getPrincipal().getId()));
        bo.setCollectCount(countRedisService.getCount("article.collect", article.getId()));
        bo.setViewCount(countRedisService.getCount("article.visit", article.getId()));
        return bo;
    }

    @Override
    public ArticleBO getArticle(Long articleId) {
        var article = getArticleById(articleId);
        return convertArticle(article);
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> SimpleBlogException.newException("article not exist : id ", articleId));
    }

    @Override
    public void likeArticle(ArticleBO article) {
        relationRedisService.add("article.like", article.getId(), getPrincipal().getId());
    }

    @Override
    public void dislikeArticle(ArticleBO article) {
        relationRedisService.remove("article.like" + article.getId(), getPrincipal().getId());
    }

    @Transactional
    @Override
    public void lockArticle(ArticleBO article) {
        articleRepository.updateArticleStatus(article.getId(), ArticleStatus.LOCKED);
    }

    @Transactional
    @Override
    public void unlockArticle(ArticleBO article) {
        articleRepository.updateArticleStatus(article.getId(), ArticleStatus.PUBLIC);
    }

    @Transactional
    @Override
    public ArticleEditBO createArticle() {
        var article = articleRepository.findByUserAndStatus(getPrincipal().getUser(), ArticleStatus.TEMP);
        if (article == null) {
            article = new Article();
            article.setStatus(ArticleStatus.TEMP);
            articleRepository.save(article);
        }
        ArticleEditBO res = new ArticleEditBO();
        res.setId(article.getId());
        return res;
    }

    @Override
    public ArticleEditBO editArticle(ArticleBO article) {
        ArticleEditBO res = new ArticleEditBO();
        var draft = articleDraftRepository.findById(article.getId());
        if (draft.isPresent()) {
            copyBean(draft, res);
        } else {
            var articlePo = getArticleById(article.getId());
            copyBean(articlePo, res);
            res.setCategoryId(article.getCategory().getId());
        }
        return res;
    }

    @Transactional
    @Override
    public void deleteArticle(ArticleBO article) {
        Long articleId = article.getId();

        articleRepository.deleteById(articleId);
        articleDraftRepository.deleteById(articleId);
        articleHistoryRepository.deleteAllByArticleId(articleId);

        relationRedisService.remove("article.like", articleId);
        countRedisService.remove("article.collect", articleId);
        countRedisService.remove("article.visit", articleId);
        articleEsRepository.deleteByArticleId(articleId);

        eventPublisher.publishEvent(new ArticleDeletedEvent(this, article));
    }

    @Transactional
    @Override
    public void saveDraft(ArticleEditBO article) {
        var articlePo = getArticleById(article.getId());
        if (articlePo.getStatus() == ArticleStatus.TEMP) {
            articlePo.setStatus(ArticleStatus.DRAFT);
            articleRepository.save(articlePo);
        }

        var po = articleDraftRepository.findById(article.getId()).orElseGet(ArticleDraft::new);
        po = copyBean(article, po);
        po.setLastModifiedTime(new Date());
        articleDraftRepository.save(po);
    }

    @Transactional
    @Override
    public void releaseArticle(ArticleEditBO article) {
        var po = getArticleById(article.getId());
        Category newCategory = categoryRepository.findById(article.getCategoryId()).get();
        var now = new Date();
        if (po.getStatus().equals(ArticleStatus.TEMP) ||
                po.getStatus().equals(ArticleStatus.DRAFT)) {
            ArticleHistory history = new ArticleHistory(article.getId(), ArticleHistoryType.CREATE);
            articleHistoryRepository.save(history);
            po.setReleaseTime(now);
        } else {
            if (!po.getTitle().equals(article.getTitle())) {
                ArticleHistory history = new ArticleHistory(article.getId(), ArticleHistoryType.MODIFY_TITLE);
                history.setPreText(po.getTitle());
                history.setAfterText(article.getTitle());
                articleHistoryRepository.save(history);
            }
            if (!po.getAbstractText().equals(article.getAbstractText())) {
                ArticleHistory history = new ArticleHistory(article.getId(), ArticleHistoryType.MODIFY_ABSTRACT);
                history.setPreText(po.getAbstractText());
                history.setAfterText(article.getAbstractText());
                articleHistoryRepository.save(history);
            }
            if (!po.getContentText().equals(article.getContentText())) {
                ArticleHistory history = new ArticleHistory(article.getId(), ArticleHistoryType.MODIFY_ARTICLE);
                history.setPreText(po.getContentText());
                history.setAfterText(article.getContentText());
                articleHistoryRepository.save(history);
            }
            if (!po.getCategory().getId().equals(article.getCategoryId())) {
                ArticleHistory history = new ArticleHistory(article.getId(), ArticleHistoryType.CHANGE_CATEGORY);
                history.setPreText(po.getCategory().getName());
                history.setAfterText(newCategory.getName());
                articleHistoryRepository.save(history);
            }
        }

        po.setTitle(article.getTitle());
        po.setAbstractText(article.getAbstractText());
        po.setContentText(article.getContentText());
        po.setCategory(newCategory);
        po.setLastModifiedTime(now);
        articleRepository.save(po);

        saveElasticSearch(po);
        eventPublisher.publishEvent(new ArticleReleasedEvent(this, convertArticle(po)));
    }

    private void saveElasticSearch(Article article) {
        articleEsRepository.deleteByArticleId(article.getId());
        ArticleEs articleEs = copyBean(article, new ArticleEs());
        articleEs.setArticleId(article.getId());
        articleEsRepository.save(articleEs);
    }

    @Override
    public List<ArticleHistory> listArticleHistory(ArticleBO article) {
        List<ArticleHistory> res = articleHistoryRepository.findAllByAndArticleIdOrderByTimeKey(article.getId());
        return res;
    }

    @Override
    public Page<ArticleBO> searchArticle(SearchType type, String text, Pageable option) {
        Page<Long> searchRes;
        switch (type) {
            case TITLE:
                searchRes = articleEsRepository.findArticleIdByTitleContaining(text, option);
                break;
            case TITLE_CONTENT:
                searchRes = articleEsRepository.findArticleIdByContentTextContainingOrAbstractTextContaining(
                        text, option);
                break;
            default:
                searchRes = articleEsRepository.findArticleIdByContentTextContainingOrAbstractTextContaining(
                        text, option);
        }
        List<ArticleBO> bos = new ArrayList<>(searchRes.getSize());
        articleRepository.findAllById(searchRes.getContent()).forEach(a -> bos.add(convertArticle(a)));
        return new PageImpl<>(bos, option, searchRes.getTotalElements());
    }

    @Override
    public Page<ArticleBO> rankArticle(ArticleRankType type, Pageable option) {
        return null;
    }

    @Override
    public Page<ArticleBO> listCategoryArticles(Long categoryId, Pageable option) {
        return convertPage(articleRepository.findAllByCategory(categoryId, option), this::convertArticle);
    }

    @Override
    public Page<ArticleBO> listCollectionArticles(Long collectionId, Pageable option) {
        return convertPage(articleRepository.findArticleByCollectionId(collectionId, option), this::convertArticle);
    }

    @EventListener
    public void onCategoryDeleted(CategoryDeletedEvent event) {
        List<Long> articleIds = articleRepository.listIdByCategoryId(event.getCategory().getId());
        for (Long articleId : articleIds) {
            ArticleBO articleBO = getArticle(articleId);
            deleteArticle(articleBO);
        }
    }

    @EventListener
    public void onArticleCollected(ArticleCollectedEvent event) {

    }

    @EventListener
    public void onCollectionDeleted(CollectionDeletedEvent collection) {

    }
}
