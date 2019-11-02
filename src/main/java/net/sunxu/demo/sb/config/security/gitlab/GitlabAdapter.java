package net.sunxu.demo.sb.config.security.gitlab;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.web.client.HttpClientErrorException;

/**
 * gitlab 接口的adapter
 */
public class GitlabAdapter implements ApiAdapter<Gitlab> {

    @Override
    public boolean test(Gitlab gitlab) {
        try {
            gitlab.getUserProfile();
            return true;
        } catch (HttpClientErrorException e) {
            return false;
        }
    }

    @Override
    public void setConnectionValues(Gitlab gitlab, ConnectionValues values) {
        GitlabUserProfile profile = gitlab.getUserProfile();
        values.setProviderUserId(String.valueOf(profile.getId()));
//        values.setDisplayName(profile.getScreenName());
//        values.setProfileUrl(profile.getUrl());
//        values.setImageUrl(profile.getProfileImageUrl());
    }

    @Override
    public UserProfile fetchUserProfile(Gitlab gitlab) {
        GitlabUserProfile profile = gitlab.getUserProfile();
        return new UserProfileBuilder()
//                .setName(profile.getScreenName())
//                .setUsername(profile.getName())
                .build();
    }

    @Override
    public void updateStatus(Gitlab gitlab, String message) {
        // not supported
    }
}
