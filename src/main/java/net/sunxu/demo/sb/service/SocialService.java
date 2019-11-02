package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.UserBO;
import net.sunxu.demo.sb.entity.User;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SocialService {

    Set<String> getSocialUserIds(String providerId, Set<String> providerUserIds);

    Long createUser(UserProfile userProfile);

    List<Connection<?>> getConnections(Long userId);

    Connection<?> getConnection(Long userId, String providerId);

    MultiValueMap<String, Connection<?>> getConnections(MultiValueMap<String, String> providerUserIds);

    void addConnection(String providerId, Connection connection);

    void updateConnection(Connection<?> connection);

    void removeConnection(Long userId, String providerId);

}
