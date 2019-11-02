package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.ArticleBO;
import net.sunxu.demo.sb.bo.CategoryBO;
import net.sunxu.demo.sb.bo.UserBO;
import net.sunxu.demo.sb.entity.Article;
import net.sunxu.demo.sb.entity.Category;
import net.sunxu.demo.sb.exception.SimpleBlogException;
import net.sunxu.demo.sb.repository.ArticleRepository;
import net.sunxu.demo.sb.repository.CategoryRepository;
import net.sunxu.demo.sb.service.event.CategoryDeletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.Id;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;

@Service
public class CategoryServiceImpl implements ApplicationEventPublisherAware, CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CountRedisService countRedisService;
    @Autowired
    private UserService userService;

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    public CategoryBO getCategory(Long categoryId) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> SimpleBlogException.newException("Cannot find category %d", categoryId));
        CategoryBO res = copyBean(category, new CategoryBO());
        res.setCreator(userService.getUserById(category.getCreatorId()));
        res.setArticleCount(countRedisService.getCount("category", categoryId));
        return res;
    }

    @Override
    public List<CategoryBO> listCategories(Long userId) {
        var categories = categoryRepository.findAllByCreatorId(userId);
        var res = categories.stream()
                .map(c -> copyBean(c, new CategoryBO()))
                .collect(Collectors.toList());
        userService.setUsers(res, (i, cat) -> categories.get(i).getCreatorId(), (cat, user) -> cat.setCreator(user));
        return res;
    }

    @Override
    public void addCategory(CategoryBO category) {
        var res = copyBean(category, new Category());
        res.setCreatorId(category.getCreator().getId());
        categoryRepository.save(res);
    }

    @Override
    public void updateCategory(CategoryBO category) {
        var res = copyBean(category, new Category());
        res.setCreatorId(category.getCreator().getId());
        categoryRepository.save(res);
    }

    @Override
    public void deleteCategory(CategoryBO category) {
        Long categoryId = category.getId();
        categoryRepository.deleteById(categoryId);
        countRedisService.remove("category", categoryId);

        eventPublisher.publishEvent(new CategoryDeletedEvent(this, category));
    }
}
