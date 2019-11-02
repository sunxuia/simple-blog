package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.CategoryBO;
import net.sunxu.demo.sb.bo.CollectionBO;
import net.sunxu.demo.sb.entity.ArticleCollect;
import net.sunxu.demo.sb.entity.Category;
import net.sunxu.demo.sb.entity.Collection;
import net.sunxu.demo.sb.exception.SimpleBlogException;
import net.sunxu.demo.sb.repository.ArticleCollectRepository;
import net.sunxu.demo.sb.repository.CollectionRepository;
import net.sunxu.demo.sb.service.event.ArticleCollectedEvent;
import net.sunxu.demo.sb.service.event.CollectionDeletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;

@Service
public class CollectionServiceImpl implements ApplicationEventPublisherAware, CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private ArticleCollectRepository articleCollectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CountRedisService countRedisService;

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    public CollectionBO getCollection(Long collectionId) {
        var collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> SimpleBlogException.newException("Cannot find collection by ", collectionId));
        var res = copyBean(collection, new CollectionBO());
        res.setCreator(userService.getUserById(collection.getCreatorId()));
        return res;
    }

    @Override
    public List<CollectionBO> listCollections(Long userId) {
        var collections = collectionRepository.findAllByCreatorIdOrderByCreateTime(userId);
        var res = collections.stream().map(c -> copyBean(c, new CollectionBO())).collect(toList());
        userService.setUsers(res, (i, u) -> collections.get(i).getCreatorId(), (c, u) -> c.setCreator(u));
        return res;
    }

    @Override
    public void addCollection(CollectionBO collection) {
        var res = new Collection();
        res.setCreateTime(new Date());
        res.setName(collection.getName());
        res.setRemark(collection.getRemark());
        res.setCreatorId(collection.getCreator().getId());
        collectionRepository.save(res);
    }

    @Override
    public void updateCollection(CollectionBO collection) {
        var res = new Collection();
        res.setName(collection.getName());
        res.setRemark(collection.getRemark());
        collectionRepository.save(res);
    }

    @Override
    public void deleteCollection(CollectionBO collection) {
        collectionRepository.deleteById(collection.getId());
        eventPublisher.publishEvent(new CollectionDeletedEvent(this, collection));
    }

    @Override
    public void collectArticle(Long collectionId, Long articleId) {
        ArticleCollect articleCollect = new ArticleCollect();
        articleCollect.setArticleId(articleId);
        articleCollect.setCollectionId(collectionId);
        articleCollect.setTime(new Date());
        articleCollectRepository.save(articleCollect);
        eventPublisher.publishEvent(new ArticleCollectedEvent(this, collectionId, articleId, true));
    }

    @Override
    public void decollectArticle(Long collectionId, Long articleId) {
        articleCollectRepository.deleteByCollectionIdAndArticleId(collectionId, articleId);
        eventPublisher.publishEvent(new ArticleCollectedEvent(this, collectionId, articleId, false));
    }

    @Override
    public boolean isCollected(Long collectionId, Long articleId) {
        return articleCollectRepository.existsByArticleIdAndCollectionId(articleId, collectionId);
    }
}
