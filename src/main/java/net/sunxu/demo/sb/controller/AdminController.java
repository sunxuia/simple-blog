package net.sunxu.demo.sb.controller;

import net.sunxu.demo.sb.bo.ArticleBO;
import net.sunxu.demo.sb.bo.UserBO;
import net.sunxu.demo.sb.config.security.Resource;
import net.sunxu.demo.sb.service.ArticleService;
import net.sunxu.demo.sb.service.SearchType;
import net.sunxu.demo.sb.service.UserManageService;
import net.sunxu.demo.sb.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Resource("admin")
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private ArticleService articleService;

    @Resource("article.lock")
    @PostMapping("/article/{id}/lock")
    public String lockArticle(@PathVariable("id") Long id) {
        ArticleBO articleBo = articleService.getArticle(id);
        if (articleBo.isLiked()) {
            articleService.lockArticle(articleBo);
        } else {
            articleService.unlockArticle(articleBo);
        }
        return "redirect:/article/" + id;
    }

    @Resource("user.list")
    @GetMapping("/users")
    public ModelAndView userList(@RequestParam(defaultValue = "registerTime") String sortType,
                                 @RequestParam(defaultValue = "false") Boolean asc,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "20") Integer count) {
        ModelAndView res = new ModelAndView("admin/users");

        res.addObject("sortType", sortType);
        res.addObject("asc", asc);

        count = count < 1 ? 1 : count;
        res.addObject("count", count);
        page = page < 1 ? 1 : page;
        res.addObject("page", page);


        Pageable option = PageRequest.of(page, count, asc ? Sort.Direction.ASC : Sort.Direction.DESC, "sortType");
        Page<UserBO> users = userManageService.listUsers(option);
        res.addObject("users", users);
        res.addObject("pageCount", users.getTotalPages());

        return res;
    }

    @PutMapping("/lock-user")
    @ResponseBody
    public void lockUser(Long userId, boolean isLock) {
        UserBO user = userManageService.getUser(userId);
        if (user.isLocked() != isLock) {
            if (isLock) {
                userManageService.lockUser(user);
            } else {
                userManageService.unlockUser(user);
            }
        }
    }
}
