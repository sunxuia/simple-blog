package net.sunxu.demo.sb.controller;

import net.sunxu.demo.sb.bo.CollectionBO;
import net.sunxu.demo.sb.service.ArticleService;
import net.sunxu.demo.sb.service.CollectionService;
import net.sunxu.demo.sb.vo.ArticleBriefVO;
import net.sunxu.demo.sb.vo.CollectionVO;
import net.sunxu.demo.sb.vo.UserBreifVO;
import org.hibernate.annotations.CollectionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;
import static net.sunxu.demo.sb.util.SbObjectUtils.copyBeans;
import static net.sunxu.demo.sb.util.SbObjectUtils.getProperties;
import static net.sunxu.demo.sb.util.SbWebUtils.*;

@Controller
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;
    @Autowired
    private ArticleService articleService;

    @GetMapping(path = {"", "/"})
    public ModelAndView collections() {
        ModelAndView res = new ModelAndView("collection/collection");

        var bos = collectionService.listCollections(getPrincipal().getId());
        var vos = copyBeans(bos, CollectionVO.class);
        res.addObject("collections", vos);

        return res;
    }

    @PostMapping("/")
    public ModelAndView addCollection(CollectionVO collection) {
        if (isAjaxRequest()) {
            var bo = copyBean(collection, new CollectionBO());
            collectionService.addCollection(bo);
            collection.setId(bo.getId());
            ModelAndView res = new ModelAndView(new MappingJackson2JsonView());
            res.addAllObjects(getProperties(collection, CollectionVO.class));
            return res;
        } else {
            return new ModelAndView("redirect:/collection");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteCollection(@PathVariable("id") Long collectionId) {
        var bo = collectionService.getCollection(collectionId);
        collectionService.deleteCollection(bo);
    }

    @PutMapping("/")
    public String updateCollection(CollectionVO collectionVO) {
        var bo = collectionService.getCollection(collectionVO.getId());
        bo = copyBean(collectionVO, new CollectionBO());
        collectionService.updateCollection(bo);
        return "redirect:/collection";
    }

    @GetMapping("/{id}")
    public ModelAndView viewCollection(@PathVariable("id") Long collectionId,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "20") Integer count) {
        ModelAndView res = new ModelAndView("collection/collection-view");

        var collectionBO = collectionService.getCollection(collectionId);
        var collectionVO = copyBean(collectionBO, new CollectionVO());
        res.addObject("collection", collectionVO);

        res.addObject("isOwner", collectionBO.getCreator().getId().equals(getPrincipal().getId()));

        count = count < 1 ? 1 : count;
        res.addObject("count", count);
        int pageCount = (collectionVO.getArticleCount() - 1) / count + 1;
        res.addObject("pageCount", pageCount);
        page = page < 1 ? 1 : page > pageCount ? pageCount : page;
        res.addObject("page", page);

        Pageable option = PageRequest.of(page, count);
        var articles = articleService.listCollectionArticles(collectionId, option);
        var vos = new ArrayList<ArticleBriefVO>(articles.getSize());
        for (int i = 0; i < articles.getSize(); i++) {
            var vo = copyBean(articles.getContent().get(i), new ArticleBriefVO());
            vo.setAuthor(copyBean(articles.getContent().get(i), new UserBreifVO()));
            vos.add(vo);
        }
        res.addObject("articles", vos);

        return res;
    }

    @PostMapping("/{id}/{article-id}")
    @ResponseBody
    public void collectArticle(@PathVariable("id") Long collectionId, @PathVariable("article-id") Long articleId,
                               @RequestParam("collect") Boolean collect) {
        if (collect) {
            collectionService.collectArticle(collectionId, articleId);
        } else {
            collectionService.decollectArticle(collectionId, articleId);
        }
    }
}
