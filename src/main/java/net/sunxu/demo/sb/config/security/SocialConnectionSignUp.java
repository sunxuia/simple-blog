package net.sunxu.demo.sb.config.security;

import net.sunxu.demo.sb.service.SocialService;
import net.sunxu.demo.sb.service.UserService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * 社交登录隐式注册接口
 */
class SocialConnectionSignUp implements ConnectionSignUp {

    private SocialService socialService;

    public SocialConnectionSignUp(SocialService socialService) {
        this.socialService = socialService;
    }

    @Override
    public String execute(Connection<?> connection) {
        UserProfile profile = connection.fetchUserProfile();
        var userId = socialService.createUser(profile);
        return userId.toString();
    }
}
