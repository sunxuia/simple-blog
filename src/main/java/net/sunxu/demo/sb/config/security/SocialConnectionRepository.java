package net.sunxu.demo.sb.config.security;

import net.sunxu.demo.sb.service.SocialService;
import org.springframework.social.connect.*;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

public class SocialConnectionRepository implements ConnectionRepository {

    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final Long userId;
    private final SocialService socialService;

    public SocialConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator,
                                      Long userId,
                                      SocialService socialService) {
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.userId = userId;
        this.socialService = socialService;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        MultiValueMap<String, Connection<?>> res = new LinkedMultiValueMap<String, Connection<?>>();
        Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
        for (String registeredProviderId : registeredProviderIds) {
            res.put(registeredProviderId, Collections.emptyList());
        }
        var connections = socialService.getConnections(userId);
        for (Connection<?> connection : connections) {
            String providerId = connection.getKey().getProviderId();
            if (res.get(providerId).size() == 0) {
                res.put(providerId, new LinkedList<>());
            }
            res.add(providerId, connection);
        }
        return res;
    }

    @Override
    public List<Connection<?>> findConnections(String providerId) {
        return List.of(socialService.getConnection(userId, providerId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        List<?> providerConnections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) providerConnections;
    }

    private <A> String getProviderId(Class<A> apiType) {
        return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
    }

    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
        Assert.notEmpty(providerUserIds, "Provider user IDs cannot be empty.");
        return socialService.getConnections(providerUserIds);
    }

    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {
        MultiValueMap<String, String> para = new LinkedMultiValueMap<>(1);
        para.add(connectionKey.getProviderId(), connectionKey.getProviderUserId());
        var connections = socialService.getConnections(para);
        if (connections.isEmpty()) {
            throw new NoSuchConnectionException(connectionKey);
        } else {
            return connections.getFirst(connectionKey.getProviderId());
        }
    }

    @SuppressWarnings("unchecked")
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        return (Connection<A>) getConnection(new ConnectionKey(getProviderId(apiType), providerUserId));
    }

    @Override
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        Connection<A> primaryConnection = findPrimaryConnection(apiType);
        if (primaryConnection == null) {
            throw new NotConnectedException(getProviderId(apiType));
        }
        return primaryConnection;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) socialService.getConnection(userId, providerId);
    }

    @Override
    public void addConnection(Connection<?> connection) {
        try {
            ConnectionKey connectionKey = connection.getKey();
            getConnection(connectionKey);
            throw new DuplicateConnectionException(connectionKey);
        } catch (NoSuchConnectionException e) {
            socialService.addConnection(connection.createData().getProviderId(), connection);
        }
    }

    @Override
    public void updateConnection(Connection<?> connection) {
        socialService.updateConnection(connection);
    }

    @Override
    public void removeConnections(String providerId) {
        socialService.removeConnection(userId, providerId);
    }

    @Override
    public void removeConnection(ConnectionKey connectionKey) {
        socialService.removeConnection(userId, connectionKey.getProviderId());
    }
}
