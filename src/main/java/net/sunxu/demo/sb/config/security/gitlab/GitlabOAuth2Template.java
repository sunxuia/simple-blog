package net.sunxu.demo.sb.config.security.gitlab;

import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.util.Map;

public class GitlabOAuth2Template extends OAuth2Template {
    public GitlabOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
    }

    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        Map<String, Object> result = (Map<String, Object>)
                getRestTemplate().postForObject(accessTokenUrl, parameters, Map.class, new Object[0]);
        if (result == null) {
            throw new RestClientException("access token endpoint returned empty result");
        } else {
            return new AccessGrant(
                    (String) result.get("access_token"),
                    (String) result.get("scope"),
                    (String) result.get("refresh_token"),
                    null);
        }
    }
}
