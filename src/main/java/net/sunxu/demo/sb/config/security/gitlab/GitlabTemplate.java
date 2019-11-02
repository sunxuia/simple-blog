package net.sunxu.demo.sb.config.security.gitlab;

import org.codehaus.jackson.map.util.ISO8601DateFormat;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * gitlab接口的实现
 */
public class GitlabTemplate extends AbstractOAuth2ApiBinding implements Gitlab {

    private String accessToken;

    public GitlabTemplate(String accessToken) {
        super(accessToken);
        this.accessToken = accessToken;
    }

    /**
     * 获取用户基本信息
     *
     * @return
     */
    @Override
    public GitlabUserProfile getUserProfile() {
        String url = "https://gitlab.com/api/v4/user";
        Map<String, Object> result = getRestTemplate().getForObject(url, Map.class);

        GitlabUserProfile userInfo = new GitlabUserProfile();
        userInfo.setId(Long.parseLong(result.get("id").toString()));
        userInfo.setAvatarUrl((String) result.get("avatar_url"));
        userInfo.setName((String) result.get("name"));
        userInfo.setUsername((String) result.get("username"));
        userInfo.setWebUrl((String) result.get("web_url"));
        userInfo.setEmail((String) result.get("email"));
        return userInfo;
    }
}
