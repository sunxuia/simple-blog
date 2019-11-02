package net.sunxu.demo.sb.config.security.gitlab;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * 获得gitlab的连接的工厂
 */
public class GitlabConnectionFactory extends OAuth2ConnectionFactory<Gitlab> {

    public GitlabConnectionFactory(String clientId, String clientSecret) {
        super("gitlab", new GitlabServiceProvider(clientId, clientSecret), new GitlabAdapter());
    }
}
