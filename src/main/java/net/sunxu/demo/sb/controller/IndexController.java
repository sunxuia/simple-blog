package net.sunxu.demo.sb.controller;

import net.sunxu.demo.sb.bo.UserBO;
import net.sunxu.demo.sb.entity.User;
import net.sunxu.demo.sb.service.*;
import net.sunxu.demo.sb.util.SbWebUtils;
import net.sunxu.demo.sb.vo.ArticleBriefVO;
import net.sunxu.demo.sb.vo.UserBreifVO;
import net.sunxu.demo.sb.vo.UserVO;
import org.apache.logging.log4j.util.Strings;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;

@Controller
public class IndexController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserManageService userManageService;

    @GetMapping("/")
    public String index() {
        return "index/index";
    }

    @GetMapping(path = {"redirect"})
    public ModelAndView redirectView() {
        String referer = SbWebUtils.getRequest().getHeader("Referer");
        if (Strings.isEmpty(referer)) {
            return new ModelAndView("redirect:/user/" + SbWebUtils.getPrincipal().getId());
        } else {
            referer = SbWebUtils.getPath(referer);
            if (referer.endsWith("/redirect") || referer.endsWith("/redirect/")) {
                referer = "/";
            }
            ModelAndView res = new ModelAndView("index/redirect-view");
            res.addObject("referer", referer);
            return res;
        }
    }

    @GetMapping("/index/article-list")
    @ResponseBody
    public List<ArticleBriefVO> getArticleList(Date lastTime, Integer count) {
        var bos = articleService.listArticles(lastTime, count);
        var res = new ArrayList<ArticleBriefVO>(bos.getSize());
        for (int i = 0; i < bos.getContent().size(); i++) {
            var vo = copyBean(bos.getContent().get(i), new ArticleBriefVO());
            vo.setAuthor(copyBean(bos.getContent().get(i), new UserBreifVO()));
            res.add(vo);
        }
        return res;
    }

    @GetMapping("/search")
    public ModelAndView search(String keyText,
                               @RequestParam(defaultValue = "TITLE_CONTENT") SearchType type,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "20") Integer count) {
        ModelAndView modelAndView = new ModelAndView("index/search");
        if (Strings.isBlank(keyText)) {
            showSearchPage(modelAndView);
        } else {
            showSearchResult(modelAndView, keyText, type, page, count);
        }
        return modelAndView;
    }

    private void showSearchPage(ModelAndView modelAndView) {
        modelAndView.addObject("isSearchResult", false);
    }

    private void showSearchResult(ModelAndView modelAndView,
                                  String keyText, SearchType type, Integer page, Integer count) {

        count = count < 1 ? 1 : count;
        modelAndView.addObject("count", count);
        page = page < 1 ? 1 : page;
        modelAndView.addObject("page", page);

        var bos = articleService.searchArticle(type, keyText, PageRequest.of(page, count));
        var res = new ArrayList<ArticleBriefVO>(bos.getSize());
        for (int i = 0; i < bos.getSize(); i++) {
            var vo = copyBean(bos.getContent().get(i), new ArticleBriefVO());
            vo.setAuthor(copyBean(bos.getContent().get(i), new UserBreifVO()));
            res.add(vo);
        }
        modelAndView.addObject("results", bos);
        modelAndView.addObject("pageCount", bos.getTotalPages());
        modelAndView.addObject("isSearchResult", true);
        modelAndView.addObject("total", bos.getTotalElements());
        modelAndView.addObject("keyText", keyText);
        modelAndView.addObject("type", type);

    }

    @GetMapping("/index/advice-search")
    @ResponseBody
    public List<Pair<String, String>> adviseSearch() {
        List<Pair<String, String>> res = new ArrayList<>(3);
        res.add(new Pair<>("article",
                "/search?keyText=article&type=title-content&count=20&page=1"));
        res.add(new Pair<>("how to create this page",
                "/search?keyText=how to create this page&type=title-content&count=20&page=1"));
        res.add(new Pair<>("how to",
                "/search?keyText=how to&type=title-content&count=20&page=1"));
        return res;
    }

    @GetMapping("/rank/article")
    public ModelAndView articleRank(@RequestParam(defaultValue = "LIKE") ArticleRankType type,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "20") Integer count) {
        ModelAndView res = new ModelAndView("index/article-rank");
        res.addObject("type", type);

        count = count < 1 ? 1 : count;
        res.addObject("count", count);
        page = page < 1 ? 1 : page;
        res.addObject("page", page);

        var bos = articleService.rankArticle(type, PageRequest.of(page, count));
        var vos = new ArrayList<ArticleBriefVO>(bos.getSize());
        for (int i = 0; i < bos.getSize(); i++) {
            var vo = copyBean(bos.getContent().get(i), new ArticleBriefVO());
            vo.setAuthor(copyBean(bos.getContent().get(i), new UserBreifVO()));
            vos.add(vo);
        }
        res.addObject("articles", vos);
        res.addObject("pageCount", bos.getTotalPages());

        return res;
    }

    @GetMapping("/rank/user")
    public ModelAndView userRank(@RequestParam(defaultValue = "like") UserRankType type,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "20") Integer count) {
        ModelAndView res = new ModelAndView("index/user-rank");
        res.addObject("type", type);

        int total = 1000;
        count = count < 1 ? 1 : count;
        res.addObject("count", count);
        page = page < 1 ? 1 : page;
        res.addObject("page", page);

        Page<UserBO> users = userManageService.rankUser(type, PageRequest.of(page, count));
        res.addObject("pageCount", users.getTotalPages());
        res.addObject("users", users.getContent());

        return res;
    }
}
