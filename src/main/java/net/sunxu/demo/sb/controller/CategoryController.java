package net.sunxu.demo.sb.controller;

import net.sunxu.demo.sb.bo.CategoryBO;
import net.sunxu.demo.sb.service.ArticleService;
import net.sunxu.demo.sb.service.CategoryService;
import net.sunxu.demo.sb.service.UserService;
import net.sunxu.demo.sb.vo.ArticleBriefVO;
import net.sunxu.demo.sb.vo.CategoryVO;
import net.sunxu.demo.sb.vo.UserBreifVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;
import static net.sunxu.demo.sb.util.SbObjectUtils.copyBeans;
import static net.sunxu.demo.sb.util.SbWebUtils.getPrincipal;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleService articleService;

    @GetMapping(path = {"", "/"})
    public ModelAndView categories() {
        ModelAndView res = new ModelAndView("category/category");
        var categories = categoryService.listCategories(getPrincipal().getId());
        var categoryVos = copyBeans(categories, CategoryVO.class);

        return res;
    }

    @PostMapping("/")
    public String addCategory(CategoryVO category) {
        var bo = copyBean(category, new CategoryBO());
        categoryService.addCategory(bo);
        return "redirect:/category";
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long categoryId) {
        var category = categoryService.getCategory(categoryId);
        categoryService.deleteCategory(category);
    }

    @PutMapping("/")
    public String updateCategory(CategoryVO categoryVO) {
        var category = categoryService.getCategory(categoryVO.getId());
        category = copyBean(category, new CategoryBO());
        categoryService.updateCategory(category);
        return "redirect:/category";
    }

    @GetMapping("/{id}")
    public ModelAndView viewCategory(@PathVariable("id") Long categoryId,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "20") Integer count) {
        ModelAndView res = new ModelAndView("category/category-view");

        CategoryBO categoryBO = categoryService.getCategory(categoryId);
        CategoryVO categoryVO = copyBean(categoryBO, new CategoryVO());
        res.addObject("category", categoryVO);

        count = count < 1 ? 1 : count;
        res.addObject("count", count);
        int pageCount = (categoryVO.getArticleCount() - 1) / count + 1;
        res.addObject("pageCount", pageCount);
        page = page < 1 ? 1 : page > pageCount ? pageCount : page;
        res.addObject("page", page);

        Pageable option = PageRequest.of(page, count);
        var articles = articleService.listCategoryArticles(categoryId, option);
        var vos = new ArrayList<ArticleBriefVO>(articles.getSize());
        for (int i = 0; i < articles.getSize(); i++) {
            var vo = copyBean(articles.getContent().get(i), new ArticleBriefVO());
            vo.setAuthor(copyBean(articles.getContent().get(i), new UserBreifVO()));
            vos.add(vo);
        }
        res.addObject("articles", vos);

        return res;
    }
}
