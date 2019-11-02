package net.sunxu.demo.sb.config.security.gitlab;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * gitlab 连接的工厂
 */
public class GitlabServiceProvider extends AbstractOAuth2ServiceProvider<Gitlab> {

    public GitlabServiceProvider(String clientId, String clientSecret) {
        super(createOAuth2Template(clientId, clientSecret));
    }

    private static OAuth2Template createOAuth2Template(String clientId, String clientSecret) {
        GitlabOAuth2Template oAuth2Template = new GitlabOAuth2Template(clientId, clientSecret,
                "https://gitlab.com/oauth/authorize",
                "https://gitlab.com/oauth/token");
        oAuth2Template.setUseParametersForClientAuthentication(true);
        return oAuth2Template;
    }

    /**
     * 获得gitlab 接口
     * @param accessToken access_token, 这个access_token 在获取的时候加入了uid 的信息, 通过分号(;) 分隔
     * @return 获得的gitlab 接口
     */
    @Override
    public Gitlab getApi(String accessToken) {
        return new GitlabTemplate(accessToken);
    }
}