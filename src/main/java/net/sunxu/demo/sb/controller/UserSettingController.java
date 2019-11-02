package net.sunxu.demo.sb.controller;

import net.coobird.thumbnailator.Thumbnails;
import net.sunxu.demo.sb.bo.UserEditBO;
import net.sunxu.demo.sb.entity.FastDFSFile;
import net.sunxu.demo.sb.entity.NotifyType;
import net.sunxu.demo.sb.entity.User;
import net.sunxu.demo.sb.service.FileService;
import net.sunxu.demo.sb.service.NotifyService;
import net.sunxu.demo.sb.service.UserManageService;
import net.sunxu.demo.sb.vo.UserSettingInfoVO;
import net.sunxu.demo.sb.vo.UserSettingNotifyVO;
import net.sunxu.demo.sb.vo.UserSettingSocialBindVO;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;
import static net.sunxu.demo.sb.util.SbWebUtils.getPrincipal;

@Controller
@RequestMapping("/setting")
public class UserSettingController {

    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private FileService fileService;


    @GetMapping(path = {"", "/"})
    public ModelAndView setting(@RequestParam(name = "error", required = false) String error) {
        var res = new ModelAndView("user/user-setting");
        res.addObject("error", error);

        var userDetailBO = userManageService.getUserDetail(getPrincipal().getId());
        res.addObject("avatarUrl", userDetailBO.getAvatarUrl());
        res.addObject("userName", userDetailBO.getName());

        UserSettingInfoVO info = new UserSettingInfoVO();
        info.setCity(userDetailBO.getCity());
        info.setGender(userDetailBO.getGender());
        info.setSelfIntroduction(userDetailBO.getSelfIntroduction());
        res.addObject("userInfo", info);

        UserSettingNotifyVO notifyVO = new UserSettingNotifyVO();
        notifyVO.setArticleLiked(notifyService.shouldReceive(NotifyType.ARTICLE_LIKED));
        notifyVO.setArticleCommented(notifyService.shouldReceive(NotifyType.ARTICLE_COMMENTED));
        notifyVO.setCommentLiked(notifyService.shouldReceive(NotifyType.COMMENT_LIKED));
        notifyVO.setCommentReplied(notifyService.shouldReceive(NotifyType.COMMENT_REPLIED));
        res.addObject("notify", notifyVO);

        Map<String, List<Connection<?>>> connections = connectionRepository.findAllConnections();
        UserSettingSocialBindVO bindVO = new UserSettingSocialBindVO();
        bindVO.setGithub(getConnectionUserId(connections, "github"));
        bindVO.setGitlab(getConnectionUserId(connections, "gitlab"));
        bindVO.setWeibo(getConnectionUserId(connections, "weibo"));
        bindVO.setFacebook(getConnectionUserId(connections, "facebook"));
        res.addObject("socialBind", bindVO);

        return res;
    }

    private String getConnectionUserId(Map<String, List<Connection<?>>> connections, String providerId) {
        List<Connection<?>> cons = connections.get(providerId);
        if (cons != null && cons.size() > 0) {
            return cons.get(0).getKey().toString();
        }
        return "";
    }

    @PutMapping("/notify")
    @ResponseBody
    public void notifySwitch(NotifyType notifyType, Boolean checked) {
        if (checked) {
            notifyService.setReceiveNotify(notifyType, getPrincipal().getId());
        } else {
            notifyService.setNotReceiveNotify(notifyType, getPrincipal().getId());
        }
    }

    @PostMapping("/avatar")
    @ResponseBody
    public List<String> uploadAvatar(@RequestParam("avatar") MultipartFile avatar,
                                     HttpServletRequest request) throws IOException {
        Double width = Double.valueOf(request.getParameter("width"));
        Double height = Double.valueOf(request.getParameter("height"));
        Double avatarX = Double.valueOf(request.getParameter("avatarX"));
        Double avatarY = Double.valueOf(request.getParameter("avatarY"));
        Double avatarWidth = Double.valueOf(request.getParameter("avatarWidth"));
        Double avatarHeight = Double.valueOf(request.getParameter("avatarHeight"));

        InputStream inputStream = avatar.getInputStream();
        ImageInputStream iis = ImageIO.createImageInputStream(inputStream);
        Iterator iterator = ImageIO.getImageReadersByFormatName(
                avatar.getContentType().substring("image/".length(), avatar.getContentType().length()));
        ImageReader reader = (ImageReader) iterator.next();
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(avatarX.intValue(), avatarY.intValue(),
                avatarWidth.intValue(), avatarHeight.intValue());
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        var outputStream = new ByteArrayOutputStream();
        Thumbnails.of(bi)
                .size(512, 512)
                .outputFormat("jpg")
                .toOutputStream(outputStream);
        FastDFSFile fastDFSFile = new FastDFSFile();
        fastDFSFile.setName(getPrincipal().getId().toString());
        fastDFSFile.setExt("jpg");
        fastDFSFile.setAuthorId(getPrincipal().getId());
        fileService.upload(fastDFSFile, outputStream.toByteArray());
        userManageService.updateUserAvatar(userManageService.getUser(getPrincipal().getId()), fastDFSFile);
        return List.of(fastDFSFile.getUrl());
    }

    @PostMapping("/user-name")
    @ResponseBody
    public Pair<Boolean, String> changeUserName(@RequestParam String userName) {
        var user = userManageService.getUser(getPrincipal().getId());
        userManageService.changeUserName(user, userName);
        getPrincipal().getUser().setName(userName);
        return new Pair<>(true, "");
    }

    @GetMapping("/is-user-name-exist")
    @ResponseBody
    public boolean isUserNameExist(@RequestParam String userName) {
        if (getPrincipal().getUsername().equals(userName)) {
            return false;
        }
        return userManageService.isUserNameExist(userName);
    }

    @PostMapping(path = "/user-info", consumes = "application/json")
    @ResponseBody
    public Pair<Boolean, String> changeUserInfo(@RequestBody UserSettingInfoVO userSettingInfo) {
        UserEditBO userEditBO = copyBean(userSettingInfo, new UserEditBO());
        userManageService.updateUser(userEditBO);
        User user = getPrincipal().getUser();
        user.setCity(userSettingInfo.getCity());
        user.setSelfIntroduction(userSettingInfo.getSelfIntroduction());
        user.setGender(userSettingInfo.getGender());
        return new Pair<>(true, "");
    }

    @GetMapping("/connect/{id}")
    public String conenct(@PathVariable("id") String providerId) {
        if (connectionFactoryLocator.registeredProviderIds().contains(providerId)) {
            return "redirect:/login/" + providerId;
        }
        return "redirect:/setting";
    }

    @DeleteMapping("/connect/{id}")
    public String disconnect(@PathVariable("id") String providerId) {
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);
        connectionRepository.removeConnections(providerId); // 删除连接
        return "redirect:/setting";
    }

    @DeleteMapping("/user")
    public String deleteUser() {
        var user = userManageService.getUser(getPrincipal().getId());
        userManageService.deleteUser(user);
        return "redirect:/";
    }
}
