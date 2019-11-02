package net.sunxu.demo.sb.controller;

import net.sunxu.demo.sb.bo.ArticleBO;
import net.sunxu.demo.sb.bo.CommentBO;
import net.sunxu.demo.sb.config.security.Resource;
import net.sunxu.demo.sb.entity.ArticleHistory;
import net.sunxu.demo.sb.entity.ArticleHistoryType;
import net.sunxu.demo.sb.entity.Attachment;
import net.sunxu.demo.sb.entity.Role;
import net.sunxu.demo.sb.service.*;
import net.sunxu.demo.sb.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static net.sunxu.demo.sb.util.SbObjectUtils.convertPage;
import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;
import static net.sunxu.demo.sb.util.SbObjectUtils.copyBeans;
import static net.sunxu.demo.sb.util.SbWebUtils.getPrincipal;

@Resource("article")
@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private CollectionService collectionService;
    @Autowired
    private CommentService commentService;

    @Resource("list")
    @GetMapping("/list")
    public ModelAndView articleList(
            @RequestParam(defaultValue = "releaseTime") String sortType,
            @RequestParam(defaultValue = "false") Boolean asc,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer count) {
        ModelAndView res = new ModelAndView("article/article-list");

        res.addObject("sortType", sortType);
        res.addObject("asc", asc);

        count = count < 1 ? 1 : count;
        res.addObject("count", count);
        page = page < 1 ? 1 : page;
        res.addObject("page", page);

        Pageable option = PageRequest.of(page, count, Sort.Direction.DESC, "releaseTime");
        Page<ArticleBO> articles = articleService.listArticles(option);
        var vos = convertPage(articles, article -> {
            ArticleListVO vo = copyBean(article, new ArticleListVO());
            vo.setUrl("/article/" + article.getId());
            return vo;
        });
        res.addObject("articles", vos);

        int pageCount = vos.getTotalPages();
        res.addObject("pageCount", pageCount);

        return res;
    }

    @Resource("view")
    @GetMapping("/{id}")
    public ModelAndView viewArticle(@PathVariable("id") Long articleId,
                                    @RequestParam(defaultValue = "1") Integer commentPage,
                                    @RequestParam(defaultValue = "20") Integer commentCount) {
        ModelAndView res = new ModelAndView("article/article-view");

        var article = articleService.getArticle(articleId);
        var articleVo = copyBean(article, new ArticleVO());
        articleVo.setAuthor(copyBean(article.getAuthor(), new UserBreifVO()));


        var attachments = attachmentService.listArticleAttachments(articleId)
                .stream()
                .filter(Attachment::getVisible)
                .collect(Collectors.toList());
        var attachmentVos = copyBeans(attachments, AttachmentVO.class);
        articleVo.setAttachments(attachmentVos);

        res.addObject("article", articleVo);

        var authorId = article.getAuthor().getId();
        var authorData = userManageService.getUserDetail(authorId);
        var authorVo = copyBean(authorData, new UserVO());
        res.addObject("author", authorVo);

        res.addObject("isAuthor", getPrincipal().getId().equals(authorId));
        res.addObject("isAdmin", getPrincipal().getRoleNames().contains(Role.ADMIN));

        var collections = collectionService.listCollections(getPrincipal().getId());
        var collectionVos = copyBeans(collections, ArticleCollectVO.class);
        for (ArticleCollectVO vo : collectionVos) {
            vo.setCollected(collectionService.isCollected(vo.getId(), articleId));
        }
        res.addObject("collections", collectionVos);

        commentCount = commentCount < 1 ? 1 : commentCount;
        res.addObject("commentCount", commentCount);
        int pageCount = (articleVo.getCommentCount() - 1) / commentCount + 1;
        res.addObject("commentPageCount", pageCount);
        commentPage = commentPage < 1 ? 1 : commentPage > pageCount ? pageCount : commentPage;
        res.addObject("commentPage", commentPage);
        Pageable option = PageRequest.of(commentPage, commentCount, Sort.Direction.ASC, "time");

        var comments = commentService.listArticleComments(articleId, option);
        var commentVos = copyBeans(comments.getContent(), CommentVO.class);
        for (int i = 0; i < commentVos.size(); i++) {
            var userVo = copyBean(comments.getContent().get(i).getUser(), new UserBreifVO());
            commentVos.get(i).setUser(userVo);
        }

        res.addObject("comments", commentVos);

        return res;
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable("id") Long articleId) {
        var article = articleService.getArticle(articleId);
        articleService.deleteArticle(article);
        return "redirect:/user/" + article.getAuthor().getId();
    }

    @PostMapping("/{id}/like")
    public void likeArticle(@PathVariable("id") Long articleId) {
        var article = articleService.getArticle(articleId);
        if (article.isLiked()) {
            articleService.dislikeArticle(article);
        } else {
            articleService.likeArticle(article);
        }
    }

    @PostMapping("/{id}/comment")
    public String createComment(@PathVariable("id") Long articleId, @RequestParam("text") String text) {
        CommentBO commentBO = new CommentBO();
        commentBO.setArticleId(articleId);
        commentBO.setText(text);
        commentService.createComment(commentBO);

        return redirectToViewArticle(articleId);
    }

    private String redirectToViewArticle(Long id) {
        return "redirect:/article/" + id;
    }

    @PostMapping("/reply-comment")
    public String replyComment(@RequestParam("reply-to") Long replyTo, @RequestParam("text") String text) {
        CommentBO replyToComment = commentService.getComment(replyTo);
        CommentBO newComment = new CommentBO();
        newComment.setText(text);
        newComment.setReplyTo(replyTo);
        newComment.setArticleId(replyToComment.getArticleId());
        commentService.createComment(newComment);

        return redirectToViewArticle(replyToComment.getArticleId());
    }

    @GetMapping("/{id}/history")
    public ModelAndView viewHistory(@PathVariable("id") Long articleId) {
        ModelAndView res = new ModelAndView("article/article-history");

        var article = articleService.getArticle(articleId);
        var articleVo = copyBean(article, new ArticleBriefVO());
        articleVo.setAuthor(copyBean(article.getAuthor(), new UserBreifVO()));
        res.addObject("article", articleVo);

        var authorId = article.getAuthor().getId();
        var authorData = userManageService.getUserDetail(authorId);
        var authorVo = copyBean(authorData, new UserVO());
        res.addObject("author", authorVo);

        var histories = articleService.listArticleHistory(article);
        List<ArticleHistoryVO> historyVos = new ArrayList<>(histories.size());
        for (ArticleHistory history : histories) {
            ArticleHistoryVO vo = copyBean(history, new ArticleHistoryVO());
            historyVos.add(vo);
        }
        res.addObject("histories", historyVos);

        return res;
    }

}
