package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.ArticleBO;
import net.sunxu.demo.sb.bo.CategoryBO;
import net.sunxu.demo.sb.entity.Article;
import net.sunxu.demo.sb.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryBO getCategory(Long categoryId);

    List<CategoryBO> listCategories(Long userId);

    void addCategory(CategoryBO category);

    void updateCategory(CategoryBO category);

    void deleteCategory(CategoryBO categoryId);

}
