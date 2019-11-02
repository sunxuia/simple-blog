package net.sunxu.demo.sb.config.security;

import net.sunxu.demo.sb.entity.User;
import net.sunxu.demo.sb.service.UserService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.*;

import java.net.URLEncoder;
import java.util.ArrayList;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LocalUserDetails anonymousUser = (LocalUserDetails) userService.loadUserByUsername(User.ANONYMOUS);

        http.antMatcher("/**").authorizeRequests()
                .antMatchers("/**").permitAll()
                .and().apply(springSocialConfigurer())
                .and().anonymous()
                .authorities(new ArrayList<>(anonymousUser.getAuthorities()))
                .principal(anonymousUser)
                .and().logout().logoutSuccessHandler(new RefererSuccessHandler("/"));
    }

    // SpringSocialConfigurer 的一些设置不能满足要求, 所以要重新实现
    private SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> springSocialConfigurer() {
        return new SecurityConfigurerAdapter<>() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
                UsersConnectionRepository usersConnectionRepository = getDependency(applicationContext,
                        UsersConnectionRepository.class);
                SocialAuthenticationServiceLocator authServiceLocator = getDependency(applicationContext,
                        SocialAuthenticationServiceLocator.class);
                SocialUserDetailsService socialUsersDetailsService = getDependency(applicationContext,
                        SocialUserDetailsService.class);

                SocialAuthenticationFilter filter = new SocialAuthenticationFilter(
                        http.getSharedObject(AuthenticationManager.class),
                        new AuthenticationNameUserIdSource(),
                        usersConnectionRepository,
                        authServiceLocator);
                filter.setFilterProcessesUrl("/login");
                filter.setConnectionAddedRedirectUrl("/setting");
                filter.setConnectionAddingFailureRedirectUrl("/setting?error=" + URLEncoder.encode("添加失败, 账号已经注册.","utf-8"));
                filter.setPostLoginUrl("/redirect");

                RememberMeServices rememberMe = http.getSharedObject(RememberMeServices.class);
                if (rememberMe != null) {
                    filter.setRememberMeServices(rememberMe);
                }

                http.authenticationProvider(
                        new SocialAuthenticationProvider(usersConnectionRepository, socialUsersDetailsService))
                        .addFilterBefore(postProcess(filter), AbstractPreAuthenticatedProcessingFilter.class);
            }

            private <T> T getDependency(ApplicationContext applicationContext, Class<T> dependencyType) {
                try {
                    T dependency = applicationContext.getBean(dependencyType);
                    return dependency;
                } catch (NoSuchBeanDefinitionException e) {
                    throw new IllegalStateException("spring social configure depends on " + dependencyType.getName()
                            + ". No single bean of that type found in application context.", e);
                }
            }
        };
    }
}
