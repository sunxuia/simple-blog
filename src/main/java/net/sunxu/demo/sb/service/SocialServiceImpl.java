package net.sunxu.demo.sb.service;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Set;

@Service
public class SocialServiceImpl implements SocialService {
    @Override
    public Set<String> getSocialUserIds(String providerId, Set<String> providerUserIds) {
        return null;
    }

    @Override
    public Long createUser(UserProfile userProfile) {
        return null;
    }

    @Override
    public List<Connection<?>> getConnections(Long userId) {
        return null;
    }

    @Override
    public Connection<?> getConnection(Long userId, String providerId) {
        return null;
    }

    @Override
    public MultiValueMap<String, Connection<?>> getConnections(MultiValueMap<String, String> providerUserIds) {
        return null;
    }

    @Override
    public void addConnection(String providerId, Connection connection) {

    }

    @Override
    public void updateConnection(Connection<?> connection) {

    }

    @Override
    public void removeConnection(Long userId, String providerId) {

    }
}
