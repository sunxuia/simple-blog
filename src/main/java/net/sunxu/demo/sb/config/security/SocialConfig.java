package net.sunxu.demo.sb.config.security;

import net.sunxu.demo.sb.config.security.gitlab.GitlabConnectionFactory;
import net.sunxu.demo.sb.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.*;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.weibo.connect.WeiboConnectionFactory;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {
    // 注册链接工厂
    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer,
                                       Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(new WeiboConnectionFactory(
                environment.getProperty("social.weibo.clientId"),
                environment.getProperty("social.weibo.clientSecret")));
        connectionFactoryConfigurer.addConnectionFactory(new GitHubConnectionFactory(
                environment.getProperty("social.github.clientId"),
                environment.getProperty("social.github.clientSecret")));
        connectionFactoryConfigurer.addConnectionFactory(new GitlabConnectionFactory(
                environment.getProperty("social.gitlab.clientId"),
                environment.getProperty("social.gitlab.clientSecret")));
        connectionFactoryConfigurer.addConnectionFactory(new FacebookConnectionFactory(
                environment.getProperty("social.facebook.clientId"),
                environment.getProperty("social.facebook.clientSecret")));
    }

    @Autowired
    private SocialService socialService;

    // 持久化连接的仓库.
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        UsersConnectionRepository repository = new SocialUsersConnectionRepository(connectionFactoryLocator, socialService);
        repository.setConnectionSignUp(new SocialConnectionSignUp(socialService));
        return repository;
    }

    // 通过 UserIDSource 获得每个用户的唯一性标识.
    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }
}
