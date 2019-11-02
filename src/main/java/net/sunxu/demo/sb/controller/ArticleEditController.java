package net.sunxu.demo.sb.controller;

import net.sunxu.demo.sb.bo.ArticleEditBO;
import net.sunxu.demo.sb.config.security.Resource;
import net.sunxu.demo.sb.entity.Attachment;
import net.sunxu.demo.sb.entity.FastDFSFile;
import net.sunxu.demo.sb.service.*;
import net.sunxu.demo.sb.util.SbObjectUtils;
import net.sunxu.demo.sb.vo.ArticleBriefVO;
import net.sunxu.demo.sb.vo.ArticleEditVO;
import net.sunxu.demo.sb.vo.AttachmentVO;
import net.sunxu.demo.sb.vo.CategoryVO;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;
import static net.sunxu.demo.sb.util.SbObjectUtils.copyBeans;
import static net.sunxu.demo.sb.util.SbObjectUtils.inputStream2Array;
import static net.sunxu.demo.sb.util.SbWebUtils.getPrincipal;


@Resource("article.edit")
@Controller
@RequestMapping("/article-edit")
public class ArticleEditController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;


    @GetMapping("/{id}")
    public ModelAndView editArticle(@PathVariable("id") Long articleId) {
        ModelAndView res = new ModelAndView("article/article-edit");
        var article = articleService.getArticle(articleId);
        res.addObject("isCreate", !article.isReleased());
        res.addObject("releaseTime", article.getReleaseTime());
        res.addObject("lastModifiedTime", article.getLastModifiedTime());

        var editBo = articleService.editArticle(article);
        ArticleEditVO editVo = copyBean(editBo, new ArticleEditVO());
        editVo.setVisible(article.isHasDraft());
        res.addObject("article", editVo);

        var attachments = attachmentService.listArticleAttachments(articleId);
        var attachmentVos = copyBeans(attachments, AttachmentVO.class);
        res.addObject("attachments", attachments);

        var categories = categoryService.listCategories(getPrincipal().getId());
        var categoryVos = copyBeans(categories, CategoryVO.class);
        res.addObject("categories", categoryVos);

        return res;
    }

    @PostMapping("/attachment/upload")
    @ResponseBody
    public List<AttachmentVO> upload(@RequestParam("file_data") MultipartFile file,
                                     @RequestParam("articleId") Long articleId)
            throws IOException {
        String fileName = file.getName();
        FastDFSFile fastDFSFile = new FastDFSFile();
        fastDFSFile.setName(fileName);
        fastDFSFile.setExt(file.getContentType());
        fastDFSFile.setAuthorId(getPrincipal().getId());
        fileService.upload(fastDFSFile, inputStream2Array(file.getInputStream()));

        Attachment attachment = new Attachment();
        attachment.setArticleId(articleId);
        attachment.setDfsId(fastDFSFile.getId());
        attachment.setUrl(fastDFSFile.getUrl());
        attachment.setName(fileName);
        attachmentService.addAttachment(attachment);

        var vo = copyBean(attachment, new AttachmentVO());
        return List.of(vo);
    }

    @PutMapping("/attachment/{attachmentId}")
    @ResponseBody
    public void changeAttachment(@PathVariable("attachmentId") Long attachmentId,
                                 @RequestParam(name = "newName", required = false) String newName,
                                 @RequestParam(name = "visibility", required = false) Boolean visibility) {
        var attachment = attachmentService.getAttachment(attachmentId);
        if (newName != null) {
            attachment.setName(newName);
        }
        if (visibility != null) {
            attachment.setVisible(visibility);
        }
        attachmentService.updateAttachment(attachment);
    }

    @DeleteMapping("/attachment/{attachmentId}")
    @ResponseBody
    public void deleteAttachment(@PathVariable("attachmentId") Long attachmentId) {
        var attachment = attachmentService.getAttachment(attachmentId);
        attachmentService.deleteAttachment(attachment);
    }

    @GetMapping("/create")
    public ModelAndView createArticle() {
        ModelAndView res = new ModelAndView("article/article-edit");
        res.addObject("isCreate", true);

        var articleEditBO = articleService.createArticle();
        var articleEditVO = copyBean(articleEditBO, new ArticleEditVO());
        res.addObject("article", articleEditVO);

        res.addObject("attachments", Collections.emptyList());
        addCategories(res);
        return res;
    }

    private void addCategories(ModelAndView modelAndView) {
        var categories = categoryService.listCategories(getPrincipal().getId());
        var categoryVos = copyBeans(categories, CategoryVO.class);
        modelAndView.addObject("categories", categoryVos);
    }

    @PostMapping("/release")
    public String releaseArticle(@Valid ArticleEditVO article, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forward:/save-as-draft";
        }
        ArticleEditBO articleEditBO = copyBean(article, new ArticleEditBO());
        articleService.releaseArticle(articleEditBO);
        return "redirect:/article/" + article.getId();
    }

    @PostMapping("/save-as-draft")
    public ModelAndView saveDraft(@Valid ArticleEditVO article, BindingResult bindingResult) {
        ModelAndView res = new ModelAndView("article/article-edit");

        if (bindingResult.hasErrors()) {
            res.addObject("errors", bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
            res.addObject("article", article);
            res.addObject("isCreate", false);
            var now = new Date();
            res.addObject("createTime", now);
            res.addObject("lastModifiedTime", now);
        } else {
            ArticleEditBO articleEditBO = copyBean(article, new ArticleEditBO());
            articleService.saveDraft(articleEditBO);
            var articleBo = articleService.getArticle(article.getId());

            res.addObject("article", article);
            res.addObject("isCreate", false);
            res.addObject("createTime", articleBo.getCreateTime());
            res.addObject("lastModifiedTime", articleBo.getLastModifiedTime());
        }

        var attachments = attachmentService.listArticleAttachments(article.getId());
        var attachmentVos = copyBeans(attachments, AttachmentVO.class);
        res.addObject("attachments", attachmentVos);

        addCategories(res);
        return res;
    }
}
