package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.UserBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SocialUserDetailsService;

import javax.swing.plaf.basic.BasicIconFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.*;

public interface UserService extends SocialUserDetailsService, UserDetailsService {
    UserBO getUserById(Long userId);

    Map<Long, UserBO> getUsersById(Set<Long> userIds);

    <T> void setUsers(Collection<T> collection,
                      BiFunction<Integer, T, Long> userIdSupplier, BiConsumer<T, UserBO> userConsumer);


}
