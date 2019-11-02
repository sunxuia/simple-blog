package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.CategoryBO;
import net.sunxu.demo.sb.bo.UserBO;
import net.sunxu.demo.sb.config.security.LocalUserDetails;
import net.sunxu.demo.sb.entity.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.sunxu.demo.sb.util.SbObjectUtils.copyBean;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public LocalUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        return loadUserByUsername(userId);
    }

    @Override
    public LocalUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userDetails = new LocalUserDetails(getTestUser(username));
        userDetails.setNotifyCount(100);
        return userDetails;
    }

    private User getTestUser(String userName) {
        User user = new User();
        user.setAvatarUrl("/images/default-avatar.png");
        user.setCreateIpAddress("127.0.0.1");
        user.setRegisterTime(new Date());
        user.setGender(Gender.FEMALE);
        user.setId(100L);
        user.setMailAddress("");
        user.setName(userName);
        Role role = new Role();
        if (userName.equals(User.ANONYMOUS)) {
            role.setName(Role.NORMAL);
        } else if (userName.equals(user.ADMIN)) {
            role.setName(Role.ADMIN);
        } else {
            role.setName(Role.NORMAL);
        }
        user.setRoles(Set.of(role));
        user.setSelfIntroduction("你好, 我是测试用户");
        user.setUserState(UserState.NORMAL);
        return user;
    }

    @Override
    public UserBO getUserById(Long userId) {
        return null;
    }

    @Override
    public Map<Long, UserBO> getUsersById(Set<Long> userIds) {
        return null;
    }

    @Override
    public <T> void setUsers(Collection<T> collection, BiFunction<Integer, T, Long> userIdSupplier,
                             BiConsumer<T, UserBO> userConsumer) {
        MultiValueMap<Long, T> map = new LinkedMultiValueMap<>(collection.size());
        int i = 0;
        for (T item : collection) {
            map.add(userIdSupplier.apply(i++, item), item);
        }
        var users = getUsersById(map.keySet());
        map.forEach((key, list) -> {
            var user = users.get(key);
            list.forEach(item -> userConsumer.accept(item, user));
        });
    }
}
