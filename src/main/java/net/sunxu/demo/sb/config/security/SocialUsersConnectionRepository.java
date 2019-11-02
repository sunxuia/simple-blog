package net.sunxu.demo.sb.config.security;

import net.sunxu.demo.sb.service.SocialService;
import org.springframework.social.connect.*;

import java.util.*;

public class SocialUsersConnectionRepository implements UsersConnectionRepository {

    private ConnectionFactoryLocator connectionFactoryLocator;

    private ConnectionSignUp connectionSignUp;

    private SocialService socialService;

    public SocialUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator,
                                           SocialService socialService) {
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.socialService = socialService;
    }

    @Override
    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        List<String> localUserIds = new ArrayList<>(1);
        var userIds = socialService.getSocialUserIds(connection.getKey().getProviderId(),
                Set.of(connection.getKey().getProviderUserId()));
        if (!userIds.isEmpty()) {
            localUserIds.add(userIds.iterator().next());
        } else if (connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null) {
                createConnectionRepository(newUserId).addConnection(connection);
                localUserIds.add(newUserId);
            }
        }
        return localUserIds;
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        return socialService.getSocialUserIds(providerId, providerUserIds);
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        return new SocialConnectionRepository(connectionFactoryLocator, Long.valueOf(userId), socialService);
    }
}
