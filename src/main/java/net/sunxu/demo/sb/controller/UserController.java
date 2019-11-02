package net.sunxu.demo.sb.controller;

import net.sunxu.demo.sb.bo.UserEventBO;
import net.sunxu.demo.sb.config.security.Resource;
import net.sunxu.demo.sb.entity.Notify;
import net.sunxu.demo.sb.entity.NotifyType;
import net.sunxu.demo.sb.exception.SimpleBlogException;
import net.sunxu.demo.sb.service.NotifyCategory;
import net.sunxu.demo.sb.service.NotifyService;
import net.sunxu.demo.sb.service.UserManageService;
import net.sunxu.demo.sb.service.UserService;
import net.sunxu.demo.sb.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;
import static net.sunxu.demo.sb.util.SbObjectUtils.copyBeans;
import static net.sunxu.demo.sb.util.SbWebUtils.getPrincipal;

@Resource("user")
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private NotifyService notifyService;

    @GetMapping("/{id}")
    public ModelAndView userIndex(@PathVariable("id") Long userId) {
        ModelAndView res = new ModelAndView("user/user-index");
        var user = userService.getUserById(userId);
        UserVO userVO = copyBean(user, new UserVO());
        res.addObject("curUser", userVO);

        res.addObject("isMe", user.getId().equals(getPrincipal().getId()));
        return res;
    }

    @GetMapping("/{id}/events")
    @ResponseBody
    public List<UserEventVO> events(@PathVariable("id") Long userId, Integer page, Integer count) {
        var user = copyBean(userManageService.getUser(userId), new UserBreifVO());
        var userEvents = userManageService.listUserEvents(userId, PageRequest.of(page, count));
        var res = new ArrayList<UserEventVO>(userEvents.getSize());
        for (UserEventBO userEvent : userEvents) {
            UserEventVO vo = copyBean(userEvent, new UserEventVO());
            vo.setUserName(user.getName());
            vo.setUserId(user.getId());
            var article = copyBean(userEvent.getArticle(), new ArticleBriefVO());
            var author = copyBean(userEvent.getArticle().getAuthor(), new UserBreifVO());
            article.setAuthor(author);
            vo.setArticle(article);
        }
        return res;
    }

    @GetMapping("/view-history")
    public ModelAndView userHistory() {
        ModelAndView res = new ModelAndView("user/view-history");
        return res;
    }

    @GetMapping("/view-history-list")
    @ResponseBody
    public List<ViewHistoryVO> userHistoryList(Date startTime, Date endTime) {
        var history = userManageService.getViewHistory(getPrincipal().getId(), startTime, endTime);
        var res = copyBeans(history, ViewHistoryVO.class);
        return res;
    }

    @GetMapping("/notify")
    public ModelAndView userNotify(@RequestParam(defaultValue = "user") String type) {
        Long userId = getPrincipal().getId();

        ModelAndView res = new ModelAndView("user/notify");
        res.addObject("type", type);

        List<Notify> notifies;
        if (type.equals("user")) {
            res.addObject("userNotifyCount", 0);
            res.addObject("systemNotifyCount",
                    notifyService.getUnreadCount(NotifyCategory.SYSTEM, userId));
            notifies = notifyService.readNotify(NotifyCategory.USER);
        } else if (type.equals("system")) {
            res.addObject("userNotifyCount",
                    notifyService.getUnreadCount(NotifyCategory.USER, userId));
            res.addObject("systemNotifyCount", 0);
            notifies = notifyService.readNotify(NotifyCategory.SYSTEM);
        } else {
            throw SimpleBlogException.newException("错误的通知类别: %s", type);
        }
        List<NotifyVO> vos = new ArrayList<>(notifies.size());
        for (Notify notify : notifies) {
            NotifyVO vo = copyBean(notify, new NotifyVO());
            vo.setUser(copyBean(notify.getUser(), new UserBreifVO()));
            vos.add(vo);
        }
        res.addObject("notifies", vos);

        return res;
    }

    @DeleteMapping("/notify/{id}")
    @ResponseBody
    public void deleteNotify(@PathVariable("id") Long notifyId) {
        if (notifyId == null) {
            throw SimpleBlogException.newException("unknown notify id.");
        }
        notifyService.deleteNotify(notifyId);
    }

}
